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
import com.example.biblioteczka.ui.dashboard.BitmapConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class, Rental::class, Person::class], version =28, exportSchema = false)
@TypeConverters(Converters::class, ListConverters::class, BitmapConverters::class)
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

            var book = Book("34x4", "Wiedźmin", "A. Sapkowski", 2008, "Przygody Geralta z Rivii", "www.taniaksiazka.pl/seria/wiedzmin")
            bookDao.insert(book)
            book = Book("31x5", "J.S. Bach: Complete Organ Works", "Breitkopf & Hartel", 2008, "Trzeba grać", "www.alenuty.pl/pl/p/Jan-Sebastian-Bach-Complete-Organ-Works-in-10-Volumes-Urtext-nowe-kompletne-wydanie-utworow-organowych-Bacha-nuty-na-organy/9546?gad_source=1&gclid=CjwKCAiA44OtBhAOEiwAj4gpOWSqSS4JfHzYUW59UxeW7Bg56uVTauBLXq775vM88ZL_8S11xBboSBoCcuAQAvD_BwE")
            bookDao.insert(book)
            book = Book("34x5", "Potop", "Henryk Sienkiewicz", 1886, "Lektura szkolna ale nie tylko", "www.taniaksiazka.pl/potop-lektura-z-opracowaniem-henryk-sienkiewicz-p-3502.html")
            bookDao.insert(book)
            book = Book("34x6", "Wprowadzenie do algorytmów", "Thomas H. Cormen", 2018, "Trzeba przeczytać", "www.empik.com/wprowadzenie-do-algorytmow-cormen-thomas-h-leiserson-charles-e-rivest-ronald-stein-clifford,p1049607421,ksiazka-p?mpShopId=6482&cq_src=google_ads&cq_cmp=20574774934&cq_term=&cq_plac=&cq_net=x&cq_plt=gp&gad_source=1&gclid=CjwKCAiA44OtBhAOEiwAj4gpOTvvnHN1GxknujPExGxMWiyxybPycfb2eHPv8bp7BDJ9niavWrvN4xoC6CYQAvD_BwE&gclsrc=aw.ds")
            bookDao.insert(book)
            book = Book("34x7", "Johann Sebastian Bach", "Wolff Cristoph", 2016, "Najlepsza biografia Bacha", "www.empik.com/johann-sebastian-bach-wolff-christoph,684356,ksiazka-p?mpShopId=4350&gad_source=1&gclid=CjwKCAiA44OtBhAOEiwAj4gpOaG2AAQ09ZpibGrsx1UiQrq5AFdN46Immqei6LDbZSvBLmPA9cp59hoC-TkQAvD_BwE&gclsrc=aw.ds")
            bookDao.insert(book)

        }
    }
}
