package com.esauloff.boxboss.model.converters;

import android.arch.persistence.room.TypeConverter;

import com.esauloff.boxboss.model.BitmapSerializable;

public class BitmapConverter {

    @TypeConverter
    public static byte[] fromBitmap(BitmapSerializable bitmap) {
        return bitmap.getBytes();
    }

    @TypeConverter
    public static BitmapSerializable toBitmap(byte[] bytes) {
        return new BitmapSerializable(bytes);
    }
}

