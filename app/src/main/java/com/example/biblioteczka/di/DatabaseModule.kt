package com.example.biblioteczka.di

import android.content.Context
import androidx.room.Room
import com.example.biblioteczka.data.BookRoomDatabase
import com.example.biblioteczka.data.book.BookDao
import com.example.biblioteczka.data.person.PersonDao
import com.example.biblioteczka.data.rental.RentalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideRentalDao(database:BookRoomDatabase): RentalDao {
        return database.rentalDao()
    }

    @Provides
    fun provideBookDao(database:BookRoomDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun providePersonDao(database:BookRoomDatabase): PersonDao {
        return database.personDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): BookRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            BookRoomDatabase::class.java,
            "books.db"
        ).build()
    }
}