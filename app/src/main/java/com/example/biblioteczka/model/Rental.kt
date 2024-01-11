package com.example.biblioteczka.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.*
import com.example.biblioteczka.converters.BookConverters
import com.example.biblioteczka.converters.PersonConverters
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@TypeConverters(BookConverters::class, PersonConverters::class)
@Entity(tableName = "rental_table")
@Parcelize
data class Rental (

    @Embedded
    val book: Book,
    @Embedded
    val person: Person,
    @ColumnInfo(name = "take")
    var take_date: LocalDateTime? = null,
    @ColumnInfo(name = "plan")
    var plan_return_date: LocalDateTime?  = null,
    @ColumnInfo(name = "return")
    var return_date: LocalDateTime? = null,
    @PrimaryKey(autoGenerate = true)
    val rental_id: Long = 0
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.O)
    fun hire() {
        take_date = LocalDateTime.now()
        plan_return_date = take_date!!.plusMonths(1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun extend() {
        plan_return_date = take_date!!.plusMonths(1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun regive() {
        return_date = LocalDateTime.now()
    }

    fun accept() {

    }
}