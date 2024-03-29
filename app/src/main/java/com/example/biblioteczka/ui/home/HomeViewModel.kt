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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val bookRepository: BookRepository,
                                        private val rentalRepository: RentalRepository,
                                        private val personRepository: PersonRepository) : ViewModel() {

    var allBooks: LiveData<List<Book>> = bookRepository.allBooks.asLiveData()
    var allRentals: LiveData<List<Rental>> = rentalRepository.allRentals.asLiveData()
    var allPersons: LiveData<List<Person>> = personRepository.allPersons.asLiveData()

    var currentBook: Book? = null
    var selectedPerson: Person? = null

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookRepository.addBook(book)
        }
    }

    fun editBook(book: Book) {
        viewModelScope.launch {
            bookRepository.editBook(book)
        }
    }

    fun setCurrentBook(position: Int) {
        currentBook = allBooks.value?.get(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hireBook(notAllowedRentalAction: () -> Unit): Rental? {
        val book = currentBook!!
        val person = selectedPerson!!

        val rentalsCounter = allRentals.value?.filter { person.person_id == it.person.person_id && it.return_date == null }?.size
        rentalsCounter?.let {
            if(it >= Person.MAX_RENTALS) {
                notAllowedRentalAction.invoke()
                return null
            }
        }
        var newRental: Rental? = null
        viewModelScope.launch {
            newRental = Rental(book, person)
            newRental?.hire()
            book.rental = newRental
            val id = rentalRepository.addRental(newRental!!)
            book.hire(id)
            bookRepository.editBook(book)
        }
        return newRental
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun regiveBook() {
        val book = currentBook!!
        val rental = allRentals.value?.first {
            it.rental_id == book.currentRental
        }
        book.regive()
        viewModelScope.launch {
            rental?.let {
                rental.regive()
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

   fun resolveRentals() {
       allBooks.value?.let { books ->
           for (book in books ) {
               if(book.currentRental != null) {
                   book.rental =  allRentals.value?.find { book.currentRental == it.rental_id }
               }
           }
       }
    }

    fun deleteBook(book: Book, action: () -> Unit) {
        viewModelScope.launch {
            bookRepository.deleteBook(book)
            action.invoke()
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