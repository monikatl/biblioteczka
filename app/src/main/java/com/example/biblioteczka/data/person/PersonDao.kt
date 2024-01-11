package com.example.biblioteczka.data.person

import androidx.room.*
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM person_table")
    fun getPersons(): Flow<List<Person>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(person: Person)

    @Update
    suspend fun update(person: Person)

    @Query("DELETE FROM person_table")
    suspend fun deleteAll()
}