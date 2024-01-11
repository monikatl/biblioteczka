package com.example.biblioteczka.converters

import androidx.room.TypeConverter
import com.example.biblioteczka.model.Rental
import com.google.gson.Gson
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken

class ListConverters {

    @TypeConverter
    fun fromJson(json: String?): List<Rental>? {
        if (json == null) {
            return null
        }

        val type: Type = object : TypeToken<List<Rental>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(rentals: List<Rental>?): String? {
        return Gson().toJson(rentals)
    }

}