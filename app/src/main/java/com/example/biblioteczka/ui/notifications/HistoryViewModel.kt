package com.example.biblioteczka.ui.notifications

import androidx.lifecycle.*
import com.example.biblioteczka.data.rental.RentalRepository
import com.example.biblioteczka.model.Rental
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: RentalRepository) : ViewModel() {

    var allRentals: LiveData<List<Rental>> = repository.allRentals.asLiveData()

    fun resolveSort(position: Int) {
        when(position) {
            0 -> sortByYoungest()
            1 -> sortByOldest()
        }
    }

    private fun sortByYoungest() {
       val sortedList = allRentals.value?.sortedBy { rental -> rental.take_date}?.reversed()
        println("POSORT" + sortedList?.size.toString())
        allRentals = MutableLiveData(sortedList)
    }

    private fun sortByOldest() {
        val sortedList = allRentals.value?.sortedBy { rental -> rental.take_date }
        println("POSORT" + sortedList?.size.toString())
        allRentals = MutableLiveData(sortedList)
    }

    fun resolveFilter(position: Int) {
        when(position) {
            0 -> {}
            1 -> sortByOldest()
        }
    }
}
