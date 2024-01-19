package com.example.biblioteczka.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.BookcaseApplication
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentPersonDetailsBinding
import com.example.biblioteczka.ui.notifications.RentalListAdapter
import com.google.android.material.tabs.TabLayout


class PersonDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PersonDetailsFragment()
    }

    val viewModel: PersonDetailsViewModel by viewModels {
        PersonDetailsViewModelFactory((activity?.application as BookcaseApplication).rentalRepository)
    }
    private lateinit var binding: FragmentPersonDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_details, container, false)

        viewModel.setPerson(arguments?.getParcelable("person"))
        binding.person = viewModel.person.value

        val recyclerView = binding.recyclerView
        val rentalsAdapter = RentalListAdapter(viewModel.rentals.value ?: emptyList())

        recyclerView.adapter = rentalsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.rentals.observe(this.viewLifecycleOwner) { items ->
            items.let {
                if (binding.tab.selectedTabPosition == 0)
                    rentalsAdapter.submitList(it)
            }
        }

        viewModel.history.observe(this.viewLifecycleOwner) { items ->
            items.let {
                if (binding.tab.selectedTabPosition == 1)
                    rentalsAdapter.submitList(it)
            }
        }


        //binding.rentals.text = "Wypożyczone ${viewModel.person.value?.rentalCounter.toString()}"


        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text) {
                    "Wypożyczone" -> rentalsAdapter.submitList(viewModel.rentals.value ?: emptyList())
                    "Historia" -> rentalsAdapter.submitList(viewModel.history.value ?: emptyList())
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

        binding.call.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + viewModel.person.value?.number))
            startActivity(intent)
        }

        return binding.root
    }
}