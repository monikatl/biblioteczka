package com.example.biblioteczka.data.book

import androidx.room.*
import com.example.biblioteczka.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    //observing database changes
    @Query("SELECT * FROM book_table")
    fun getBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("DELETE FROM book_table")
    suspend fun deleteAll()

}