package com.example.biblioteczka.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.BookcaseApplication
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentHistoryBinding
import com.example.biblioteczka.ui.dashboard.PersonListAdapter
import com.example.biblioteczka.ui.home.HomeViewModel
import com.example.biblioteczka.ui.home.HomeViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel: HistoryViewModel by viewModels {
            HistoryViewModelFactory((activity?.application as BookcaseApplication).rentalRepository)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        val adapter = RentalListAdapter(historyViewModel.allRentals.value ?: emptyList())
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(context)

        historyViewModel.allRentals.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        return binding.root
    }

}