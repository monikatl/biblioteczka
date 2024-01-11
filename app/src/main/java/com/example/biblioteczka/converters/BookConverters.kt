package com.example.biblioteczka.converters

import androidx.room.TypeConverter
import com.example.biblioteczka.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookConverters {
    @TypeConverter
    fun fromJson(json: String?): Book? {
        if (json == null) {
            return null
        }

        val type = object : TypeToken<Book>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(book: Book?): String? {
        return Gson().toJson(book)
    }
}