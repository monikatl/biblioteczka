package com.example.biblioteczka.ui.dashboard

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentPersonDetailsBinding
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.ui.home.BookListAdapter
import com.example.biblioteczka.ui.notifications.RentalListAdapter

class PersonDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PersonDetailsFragment()
    }

    val viewModel: PersonDetailsViewModel by viewModels()
    private lateinit var binding: FragmentPersonDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_details, container, false)



        viewModel.setPerson(arguments?.getParcelable("person"))
        binding.person = viewModel.person.value



        val rentals = binding.rentalsRecyclerView
        //val rentalsAdapter = RentalListAdapter(viewModel.person.value?.rentals!!)

        //rentals.adapter = rentalsAdapter
        //rentals.layoutManager = LinearLayoutManager(requireContext())
//        homeViewModel.allBooks.observe(this.viewLifecycleOwner) { items ->
//            items.let {
//                adapter.submitList(it)
//            }
//        }


        val history = binding.historyRecyclerView
        //val historyAdapter = RentalListAdapter(viewModel.person.value?.rentals!!)
        //history.adapter = historyAdapter
       // history.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
}