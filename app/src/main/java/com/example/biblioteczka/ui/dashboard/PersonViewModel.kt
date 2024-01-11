package com.example.biblioteczka.ui.dashboard

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.*
import com.example.biblioteczka.data.book.BookRepository
import com.example.biblioteczka.data.person.PersonRepository
import com.example.biblioteczka.data.rental.RentalRepository
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.ui.home.HomeViewModel
import kotlinx.coroutines.*

class PersonViewModel(
    private val personRepository: PersonRepository
) : ViewModel() {

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    lateinit var contactList: LiveData<List<Person>>
    init {
        viewModelScope.launch {
            _isLoading.value = true
            val deff = async { personRepository.allPersons}
            contactList = deff.await().asLiveData()
        }
    }
}

class PersonViewModelFactory(
    private val personRepository: PersonRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonViewModel(personRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}