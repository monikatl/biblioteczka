package com.example.biblioteczka.ui.notifications

import androidx.lifecycle.*
import com.example.biblioteczka.data.rental.RentalRepository
import com.example.biblioteczka.model.Rental

class HistoryViewModel(private val repository: RentalRepository) : ViewModel() {

    var allRentals: LiveData<List<Rental>> = repository.allRentals.asLiveData()

}

class HistoryViewModelFactory(private val repository: RentalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}