package com.example.biblioteczka.ui.home

import com.example.biblioteczka.model.Book

interface BookRecyclerViewClickListener {
    fun onHireButtonClick(position: Int)
    fun onReturnButtonClick(position: Int)
    fun onAcceptButtonClick(position: Int)
    fun onShareButtonClick(position: Int)
    fun onBookItemClick(item: Book)
    fun onSiteClick(item: Book)

    fun onExclamationClick(item: Book)
}