package com.example.biblioteczka.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.BookcaseApplication
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentHistoryBinding
import com.example.biblioteczka.ui.dashboard.PersonListAdapter
import com.example.biblioteczka.ui.home.HomeViewModel
import com.example.biblioteczka.ui.home.HomeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel: HistoryViewModel by viewModels ()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        val adapter = RentalListAdapter(historyViewModel.allRentals.value ?: emptyList())
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(context)

        historyViewModel.allRentals.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it ?: emptyList())
            }
        }

//        val sortPredicates = listOf("Od najmłodszych", "Od najstarszych")
//
//        activity?.let {
//            val arrayAdapter = ArrayAdapter(
//                it,
//                android.R.layout.simple_spinner_item,
//                sortPredicates
//            )
//            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.sort.adapter = arrayAdapter
//
//            binding.sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    historyViewModel.resolveSort(position)
//                    adapter.submitList(historyViewModel.allRentals.value ?: emptyList())
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    Toast.makeText(requireContext(), "Musisz wybrać kto!!!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }

        val filterPredicates = listOf("Wszystkie", "Przekroczony termin", "Nie zwrócone")

//        activity?.let {
//            val arrayAdapter = ArrayAdapter(
//                it,
//                android.R.layout.simple_spinner_item,
//                filterPredicates
//            )
//            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.filter.adapter = arrayAdapter
//
//            binding.filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    historyViewModel.resolveFilter(position)
//                    adapter.submitList(historyViewModel.allRentals.value ?: emptyList())
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    Toast.makeText(requireContext(), "Musisz wybrać kto!!!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }



        return binding.root
    }

}