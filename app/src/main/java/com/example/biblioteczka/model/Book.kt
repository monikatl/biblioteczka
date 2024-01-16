package com.example.biblioteczka.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.*
import com.example.biblioteczka.converters.LocalDateTimeConverters
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "book_table")
@TypeConverters(LocalDateTimeConverters::class)
@Parcelize
data class Book(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val year: Int,
    val description: String,
    val site: String,
    var state: State = State.FREE,
    var currentRental: Long? = null
) : Parcelable {

    @Ignore
    var rental: Rental? = null

    fun hire(rental: Long) {
        state = State.RENTAL
        currentRental = rental
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun regive() {
        state = State.RETURNED
        currentRental = null
    }

    fun accept() {
        state = State.FREE
    }
}