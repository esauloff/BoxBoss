package com.esauloff.boxboss.item;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esauloff.boxboss.R;
import com.esauloff.boxboss.model.Item;
import com.esauloff.boxboss.storage.ItemDatabase;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ItemEditorActivity extends Activity {
    private static final int TAKE_PICTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 2;

    private static final String ITEM_EXTRA = "item";
    private static final String ITEM_ID_EXTRA = "itemId";

    private EditText nameEdit;
    private EditText commentEdit;
    private ImageView imageView;
    private Button takeImageButton;
    private Button pickColorButton;
    private Button saveButton;

    private String imagePath;
    private int color;

    private BitmapFactory.Options options = new BitmapFactory.Options();
    private AlertDialog colorPickerDialog;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private ItemDatabase itemDatabase;
    private Item item;

    /* callbacks */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        nameEdit = findViewById(R.id.edit_name);
        commentEdit = findViewById(R.id.edit_comment);
        imageView = findViewById(R.id.view_image);
        takeImageButton = findViewById(R.id.btn_takeImage);
        pickColorButton = findViewById(R.id.btn_pickColor);
        saveButton = findViewById(R.id.btn_save);

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                saveButton.setEnabled(charSequence != null && charSequence.length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            takeImageButton.setEnabled(false);
        }

        saveButton.setEnabled(false);

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Serializable extra = getIntent().getSerializableExtra(ITEM_EXTRA);
        if(extra instanceof Item) {
            item = (Item)extra;

            nameEdit.setText(item.getName());
            commentEdit.setText(item.getComment());

            imagePath = item.getImagePath();
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));

            color = item.getColor();
            pickColorButton.setBackgroundColor(color);
        }
        else {
            item = new Item();
        }

        colorPickerDialog = ColorPickerDialogBuilder.with(this).setTitle("Choose color").density(5)
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .setPositiveButton("OK", (DialogInterface dialog, int selectedColor, Integer[] allColors) -> {
                    color = selectedColor;
                    pickColorButton.setBackgroundColor(color);
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {})
                .setOnColorSelectedListener((int selectedColor) -> {}).build();

        itemDatabase = ItemDatabase.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_PERMISSION_REQUEST) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageCapture();
            }
            else {
                Toast.makeText(this, "Camera permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* actions */

    public void takeImage(View view) {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
        else {
            startImageCapture();
        }
    }

    public void pickColor(View view) {
        colorPickerDialog.show();
    }

    public void save(View view) {
        item.setName(nameEdit.getText().toString());
        item.setComment(commentEdit.getText().toString());
        item.setImagePath(imagePath);
        item.setColor(color);

        Future<Integer> future = executor.submit(() -> {
            int id = 0;

            if(item.getId() != 0) {
                item.setLastModifiedDate(new Date());
                if(itemDatabase.itemDao().update(item) == 1) {
                    id = item.getId();
                }
                else {
                    throw new Exception("An error occurred while updating item in database");
                }
            }
            else {
                id = (int)itemDatabase.itemDao().insert(item);
                if(id == -1) {
                    throw new Exception("An error occurred while inserting item into database");
                }
            }

            return id;
        });

        int id = 0;
        try {
            id = future.get(5, TimeUnit.SECONDS);
        }
        catch(ExecutionException | InterruptedException | TimeoutException exc) {
            future.cancel(true);

            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            exc.printStackTrace();
        }

        Intent intent = new Intent();
        intent.putExtra(ITEM_ID_EXTRA, id);

        setResult(RESULT_OK, intent);

        finish();
    }

    public void cancel(View view) {
        finish();
    }

    /* private */

    private void startImageCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            File image = null;

            try {
                if(imagePath != null) {
                    File previousImage = new File(imagePath);
                    if(previousImage.exists()) {
                        previousImage.delete();
                    }
                }

                image = createImageFile();
            }
            catch(IOException ex) {
                Toast.makeText(this, "Error occurred while creating image file", Toast.LENGTH_SHORT).show();
            }

            if(image != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.esauloff.boxboss.fileprovider", image);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir.getPath() + File.separator + timestamp + ".jpg");

        imagePath = image.getAbsolutePath();

        return image;
    }
}

