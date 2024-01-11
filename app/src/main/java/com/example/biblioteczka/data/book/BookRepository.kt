package com.example.biblioteczka.data.book

import com.example.biblioteczka.model.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<Book>> = bookDao.getBooks()

    suspend fun addBook(book: Book) {
        bookDao.insert(book)
    }

    suspend fun editBook(book: Book) {
        bookDao.update(book)
    }
}