package com.example.biblioteczka.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.BookcaseApplication
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentPersonBinding
import com.example.biblioteczka.model.Person

class PersonFragment : Fragment() {

    private lateinit var binding: FragmentPersonBinding
    private val personViewModel: PersonViewModel by viewModels {
        PersonViewModelFactory((activity?.application as BookcaseApplication).personRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_person, container,false)


        val adapter = PersonListAdapter {
            val bundle = bundleOf("person" to it)
            findNavController().navigate(R.id.action_navigation_person_to_personDetailsFragment, bundle)
        }

        binding.personRecyclerView.adapter = adapter
        binding.personRecyclerView.layoutManager = LinearLayoutManager(context)

        personViewModel.isLoading.observe(this.viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }
        personViewModel.contactList.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
                personViewModel._isLoading.value = false
            }
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg, adapter)
                return false
            }
        })

        return binding.root
    }

    private fun filter(text: String, adapter: PersonListAdapter) {
        val filteredList: MutableList<Person> = mutableListOf()

        for (item in personViewModel.contactList.value!!) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty())
            Toast.makeText(requireContext(), "Nie znaleziono kontaktu!", Toast.LENGTH_SHORT).show()
        else
            adapter.filterList(filteredList)
    }
}