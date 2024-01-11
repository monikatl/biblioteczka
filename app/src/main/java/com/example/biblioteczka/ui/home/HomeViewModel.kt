package com.example.biblioteczka.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.biblioteczka.data.book.BookRepository
import com.example.biblioteczka.data.person.PersonRepository
import com.example.biblioteczka.data.rental.RentalRepository
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.model.Rental
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class HomeViewModel(private val bookRepository: BookRepository,
                    private val rentalRepository: RentalRepository,
                    private val personRepository: PersonRepository) : ViewModel() {

    var allBooks: LiveData<List<Book>> = bookRepository.allBooks.asLiveData()
    var allRentals: LiveData<List<Rental>> = rentalRepository.allRentals.asLiveData()

    var currentBook: Book? = null
    var selectedPerson: Person? = null

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookRepository.addBook(book)
        }
    }

    fun setCurrentBook(position: Int) {
        currentBook = allBooks.value?.get(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hireBook() {
        viewModelScope.launch {
            val book = currentBook!!
            val person = selectedPerson!!
            val newRental = Rental(book, person)
            newRental.hire()
            val id = rentalRepository.addRental(newRental)
            book.hire(id)
            bookRepository.editBook(book)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun regiveBook() {
        val book = currentBook!!
        val rental = allRentals.value?.first {
            println(it.rental_id)
            println(book.currentRental)
            it.rental_id == book.currentRental
        }
        book.regive()
        viewModelScope.launch {
            println("RETURN_DATE $rental")
            rental?.let {
                rental.regive()
                println("RETURN_DATE ${rental.return_date}")
                rentalRepository.editRental(rental)
            }
            bookRepository.editBook(book)
        }
    }

    fun shareBook() {
        currentBook!!.accept()
        viewModelScope.launch {
            bookRepository.editBook(currentBook!!)
        }
    }
}

class HomeViewModelFactory(
    private val bookRepository: BookRepository,
    private val rentalRepository: RentalRepository,
    private val personRepository: PersonRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(bookRepository, rentalRepository, personRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}