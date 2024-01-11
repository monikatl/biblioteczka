package com.example.biblioteczka.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import androidx.room.TypeConverters
import com.example.biblioteczka.converters.ListConverters
import com.example.biblioteczka.converters.LocalDateTimeConverters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "person_table")
@TypeConverters(LocalDateTimeConverters::class)
@Parcelize
data class Person(
    val name: String,
    val number: String,
//    @ColumnInfo(name = "person_rentals")
//    val rentals: MutableList<Rental> = mutableListOf(),
    @PrimaryKey(autoGenerate = true)
    val person_id: Long = 0
): Parcelable {

//    fun addNewRental(rental: Rental) {
//        rentals.add(rental)
//    }
    override fun toString(): String {
        return "$name"
    }

    companion object {
        const val MAX_RENTALS = 10
    }
}