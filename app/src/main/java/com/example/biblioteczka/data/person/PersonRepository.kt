package com.example.biblioteczka.data.person

import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import kotlinx.coroutines.flow.Flow

class PersonRepository(val personDao: PersonDao){
    val allPersons: Flow<List<Person>> = personDao.getPersons()

    suspend fun addPerson(person: Person) {
        personDao.insert(person)
        println("Dodano$person")
    }

    suspend fun editPerson(person: Person) {
        personDao.update(person)
    }
}