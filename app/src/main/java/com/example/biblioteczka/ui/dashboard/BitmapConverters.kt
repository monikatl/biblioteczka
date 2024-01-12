package com.example.biblioteczka.ui.dashboard

import android.graphics.Bitmap

import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class BitmapConverters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): Bitmap? {
        return if (byteArray == null || byteArray.isEmpty()) {
            null
        } else BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}