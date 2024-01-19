package com.example.biblioteczka.ui.dashboard

import androidx.lifecycle.*
import com.example.biblioteczka.data.rental.RentalRepository
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.model.Rental
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(private val rentalRepository: RentalRepository) : ViewModel() {
    private val _person = MutableLiveData<Person>()
    val person: LiveData<Person> get() = _person

    var history: LiveData<List<Rental>> = rentalRepository.allRentals.map {
                    rentals -> rentals.filter {
                    rental: Rental ->  rental.person.name == person.value?.name && rental.return_date != null
                    }
                }.asLiveData()

    var rentals = rentalRepository.allRentals.map {
            rentals -> rentals.filter {
            rental: Rental ->  rental.person.name == person.value?.name && rental.return_date == null
    }
    }.asLiveData()

    fun setPerson(person: Person?) {
        person?.let {
            _person.value = person!!
        }
    }
}
