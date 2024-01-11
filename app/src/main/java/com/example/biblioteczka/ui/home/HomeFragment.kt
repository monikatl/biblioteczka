package com.example.biblioteczka.ui.home


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteczka.BookcaseApplication
import com.example.biblioteczka.MainActivity
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.FragmentHomeBinding
import com.example.biblioteczka.model.Book


class HomeFragment : Fragment(), BookRecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel:HomeViewModel by viewModels {
        HomeViewModelFactory((activity?.application as BookcaseApplication).bookRepository,
            (activity?.application as BookcaseApplication).rentalRepository,
        (activity?.application as BookcaseApplication).personRepository)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val bookRecyclerView = binding.booksRecyclerView
        val adapter = BookListAdapter()

        adapter.buttonClickListener = this

        bookRecyclerView.adapter = adapter
        bookRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        homeViewModel.allBooks.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        homeViewModel.allRentals.observe(this.viewLifecycleOwner) { items ->
            items.let {
                //adapter.submitList(it)
            }
        }

        binding.addBook.setOnClickListener { AddBookDialogFragment(homeViewModel).show(childFragmentManager, AddBookDialogFragment.TAG) }

        return binding.root
    }

    override fun onHireButtonClick(position: Int) {
        homeViewModel.setCurrentBook(position)
        showHireDialog()
    }

    override fun onReturnButtonClick(position: Int) {
        homeViewModel.setCurrentBook(position)
        showReturnDialog()
    }

    private fun showReturnDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Czy chcesz zwrócić tytuł: ${homeViewModel.currentBook?.title}?")
                .setMessage("Data zwrotu: }")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Tak") {_, _ -> regive()}
                .create()
                .show()
        }
    }

    private fun regive() {
        homeViewModel.regiveBook()
    }

    override fun onAcceptButtonClick(position: Int) {
        homeViewModel.setCurrentBook(position)
        showAcceptDialog()
    }

    private fun showAcceptDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Czy chcesz udostępnić ten tytuł do ponownego wypożyczenia: ${homeViewModel.currentBook?.title}?")
                .setMessage("Potwierdź, że pozycja została sprawdzona.")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Tak") {_, _ -> share()}
                .create()
                .show()
        }
    }

    private fun share() {
        homeViewModel.shareBook()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showHireDialog() {
        val persons = (activity as MainActivity).persons
        val inflater = LayoutInflater.from(this.requireContext())
        val view = inflater.inflate(R.layout.dialog_spinner, null)

        activity?.let {

            val spinner: Spinner = view.findViewById(R.id.spinner)
            val adapter = ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                persons
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    homeViewModel.selectedPerson = persons[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(requireContext(), "Musisz wybrać kto!!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            AlertDialog.Builder(it)
                .setTitle("Czy na pewno chcesz wypożyczyć ten tytuł?")
                .setView(view)
                .setMessage("${homeViewModel.currentBook?.author} - ${homeViewModel.currentBook?.title}")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Wypożycz") {_, _ -> hire()}
                .create()
                .show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hire() {
        homeViewModel.hireBook()
        sendSms("Wypożyczyłeś tytuł: ${homeViewModel.currentBook!!.title}", "519489463")
    }

    private fun sendSms(message: String, phoneNumber: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }

    override fun onShareButtonClick(position: Int) {
        val currentPosition = homeViewModel.allBooks.value?.get(position)
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"))
        intent.putExtra("sms_body", currentPosition.toString())
        startActivity(intent)
    }

    override fun onBookItemClick(item: Book) {
        AddBookDialogFragment(homeViewModel, item).show(childFragmentManager, AddBookDialogFragment.TAG)
    }

    override fun onSiteClick(item: Book) {
        openWebsite(item.site)
    }

    private fun openWebsite(uriText: String) {
        val url = "https://$uriText"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}