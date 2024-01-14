package com.example.biblioteczka.ui.home.fragments

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
import com.example.biblioteczka.ui.home.BookRecyclerViewClickListener
import com.example.biblioteczka.ui.home.HomeViewModel
import com.example.biblioteczka.ui.home.HomeViewModelFactory
import com.example.biblioteczka.ui.home.SendDialogFragment
import com.example.biblioteczka.ui.home.adapters.BookListAdapter
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(), BookRecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((activity?.application as BookcaseApplication).bookRepository,
            (activity?.application as BookcaseApplication).rentalRepository,
        (activity?.application as BookcaseApplication).personRepository)
    }

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
                homeViewModel.resolveRentals()
            }
        }

        homeViewModel.allRentals.observe(this.viewLifecycleOwner) { items ->
            items.let {
                homeViewModel.resolveRentals()
            }
        }

        binding.addBook.setOnClickListener { AddBookDialogFragment(homeViewModel).show(childFragmentManager, AddBookDialogFragment.TAG) }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHireButtonClick(position: Int) {
        homeViewModel.setCurrentBook(position)
        showHireDialog()
    }

    override fun onReturnButtonClick(position: Int) {
        homeViewModel.setCurrentBook(position)
        showReturnDialog()
    }

    private fun showReturnDialog() {
        var book: Book? = null
        homeViewModel.currentBook?.let {
            book = it
        }
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Zwrot")
                .setMessage("Czy chcesz zwrócić tytuł: ${book?.title}?")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Tak") {_, _ -> regive()}
                .create()
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                .setMessage("Potwierdź, że pozycja została dokładnie sprawdzona i może być ponownie udostępniona czytelnikom.")
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
                .setTitle("Czy chcesz wypożyczyć tytuł?")
                .setView(view)
                .setMessage("${homeViewModel.currentBook?.author} - ${homeViewModel.currentBook?.title}")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Wypożycz") {_, _ -> hire()}
                .create()
                .show()
        }

    }

    private fun hire() {
        val book = homeViewModel.currentBook!!
        val rental = homeViewModel.hireBook()
        rental?.let {
            sendSms("Wypożyczyłeś tytuł: ${book.author} - ${book.title}.", "519489463")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val planReturnDate = rental.plan_return_date?.format(formatter) ?: "-"
            Toast.makeText(requireContext(), "Planowana data zwrotu: $planReturnDate", Toast.LENGTH_LONG).show()
        }
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

    override fun onExclamationClick(item: Book) {
        Toast.makeText(requireContext(), "Dnia ${item.rental?.plan_return_date} upłynał termin zwrotu!", Toast.LENGTH_LONG).show()
    }

    private fun openWebsite(uriText: String) {
        val url = "https://$uriText"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun showSendDialog() {
        SendDialogFragment(homeViewModel).show(childFragmentManager, SendDialogFragment.TAG)
    }
}