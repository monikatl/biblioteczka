package com.example.biblioteczka.converters

import androidx.room.TypeConverter
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PersonConverters {
    @TypeConverter
    fun fromJson(json: String?): Person? {
        if (json == null) {
            return null
        }

        val type = object : TypeToken<Person>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(person: Person?): String? {
        return Gson().toJson(person)
    }
}