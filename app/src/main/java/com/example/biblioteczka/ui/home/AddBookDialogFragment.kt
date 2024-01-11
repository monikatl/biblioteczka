package com.example.biblioteczka.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.DialogAddBookBinding
import com.example.biblioteczka.model.Book

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

        binding.save.setOnClickListener {
            createNewBook(binding)
            addBookToDatabase()
            dismiss()
        }
        return binding.root
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