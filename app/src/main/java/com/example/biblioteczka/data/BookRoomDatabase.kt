package com.example.biblioteczka.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.biblioteczka.converters.Converters
import com.example.biblioteczka.converters.ListConverters
import com.example.biblioteczka.data.book.BookDao
import com.example.biblioteczka.data.person.PersonDao
import com.example.biblioteczka.data.rental.RentalDao
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.model.Rental
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class, Rental::class, Person::class], version =26, exportSchema = false)
@TypeConverters(Converters::class, ListConverters::class)
abstract class BookRoomDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun rentalDao(): RentalDao
    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: BookRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : BookRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookRoomDatabase::class.java,
                    "book_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(BookDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }

        }
        private class BookDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.bookDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(bookDao: BookDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            bookDao.deleteAll()

            var book = Book("34x4", "Wiedźmin", "A. Sapkowski", 2008, "Bla bla bla", "xyz@fty.pl")
            bookDao.insert(book)
            book = Book("34x5", "Wiedźmin", "A. Sapkowski", 2008, "Bla bla bla", "xyz@fty.pl")
            bookDao.insert(book)
        }
    }
}
