package com.example.biblioteczka.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.DialogAddBookBinding
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.State
import com.example.biblioteczka.ui.home.HomeViewModel

class AddBookDialogFragment(private val viewModel: HomeViewModel, book: Book? = null) : DialogFragment() {


    private var book: Book? = null
    private var isNew: Boolean = true

    init {
        this.book = book
        this.book?.let { isNew = false }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: DialogAddBookBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_add_book, container, false)


        bookBind(binding)
        binding.isNew = isNew

        binding.delete.visibility = if(book?.state != State.RENTAL) View.VISIBLE else View.GONE
        binding.delete.setOnClickListener {
            if(isNew) dismiss()
            book?.let {
                showAcceptDeleteDialog(it)
            }
        }

        binding.save.setOnClickListener {
            createNewBook(binding)
            addBookToDatabase()
            dismiss()
        }
        return binding.root
    }

    private fun showAcceptDeleteDialog(book: Book) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Czy chcesz usunąć tytuł: ${book.title}?")
                .setNegativeButton("Anuluj", null)
                .setPositiveButton("Tak") {_, _ -> viewModel.deleteBook(book) { dismiss() } }
                .create()
                .show()
        }
    }

    private fun bookBind(binding: DialogAddBookBinding) {
        book?.let {
            with(binding) {
                nameInput.setText(it.title)
                authorInput.setText(it.author)
                positionInput.setText(it.id)
                yearInput.setText(it.year.toString())
                descriptionInput.setText(it.description)
                siteInput.setText(it.site)
            }
        }
    }

    private fun createNewBook(binding: DialogAddBookBinding) {
        val position = binding.positionInput.text.toString()
        val title = binding.nameInput.text.toString()
        val author = binding.authorInput.text.toString()
        val year = binding.yearInput.text.toString().toInt()
        val description = binding.descriptionInput.text.toString()
        val site = binding.siteInput.text.toString()
        book = Book(position, title, author, year, description, site)
    }

    private fun addBookToDatabase() {
        book?.let {
            viewModel.addBook(it)
        }
    }

    companion object {
        const val TAG = "AddBookDialog"
    }
}