package com.example.biblioteczka.data.rental

import androidx.room.*
import com.example.biblioteczka.model.Rental
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {
    //observing database changes
    @Query("SELECT * FROM rental_table")
    fun getRentals(): Flow<List<Rental>>

    @Query("SELECT * FROM rental_table WHERE rental_id = :id")
    fun getRentalById(id: Long): Flow<Rental>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rental: Rental): Long

    @Update
    suspend fun update(rental: Rental)

    @Query("DELETE FROM rental_table")
    suspend fun deleteAll()
}