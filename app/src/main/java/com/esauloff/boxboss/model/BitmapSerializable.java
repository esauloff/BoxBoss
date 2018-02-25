package com.esauloff.boxboss.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class BitmapSerializable implements Serializable {
    private static final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    private final byte[] bytes;

    public BitmapSerializable(Bitmap bitmap) {
        if(bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            bytes = stream.toByteArray();
        }
        else {
            bytes = new byte[0];
        }
    }

    public BitmapSerializable(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Bitmap toBitmap() {
        if(bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }
    }
}

