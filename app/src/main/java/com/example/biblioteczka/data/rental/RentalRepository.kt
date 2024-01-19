package com.example.biblioteczka.data.rental

import androidx.lifecycle.asLiveData
import com.example.biblioteczka.model.Rental
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RentalRepository @Inject constructor(private val rentalDao: RentalDao){
    val allRentals: Flow<List<Rental>> = rentalDao.getRentals()

    suspend fun addRental(rental: Rental): Long {
        return rentalDao.insert(rental)
    }

    suspend fun getRentalById(id: Long): Rental {
        return rentalDao.getRentalById(id).asLiveData().value!!
    }

    suspend fun editRental(rental: Rental) {
        rentalDao.update(rental)
    }
}