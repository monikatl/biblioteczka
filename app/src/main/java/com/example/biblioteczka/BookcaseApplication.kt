package com.example.biblioteczka

import android.app.Application
import com.example.biblioteczka.data.book.BookRepository
import com.example.biblioteczka.data.BookRoomDatabase
import com.example.biblioteczka.data.person.PersonRepository
import com.example.biblioteczka.data.rental.RentalRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class BookcaseApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
//    val database by lazy { BookRoomDatabase.getDatabase(this, applicationScope) }
//    val bookRepository by lazy { BookRepository(database.bookDao()) }
//    val rentalRepository by lazy { RentalRepository(database.rentalDao()) }
//    val personRepository by lazy { PersonRepository(database.personDao()) }
}