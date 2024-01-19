package com.example.biblioteczka.data.person

import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonRepository @Inject constructor(private val personDao: PersonDao){
    val allPersons: Flow<List<Person>> = personDao.getPersons()

    suspend fun addPerson(person: Person) {
        personDao.insert(person)
    }

    suspend fun editPerson(person: Person) {
        personDao.update(person)
    }
}