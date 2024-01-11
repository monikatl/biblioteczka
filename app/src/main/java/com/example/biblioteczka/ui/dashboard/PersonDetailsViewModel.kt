package com.example.biblioteczka.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biblioteczka.model.Person

class PersonDetailsViewModel : ViewModel() {
    private val _person = MutableLiveData<Person>()
    val person: LiveData<Person> get() = _person

    fun setPerson(person: Person?) {
        person?.let {
            _person.value = person!!
        }
    }
}