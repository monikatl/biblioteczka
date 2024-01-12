package com.example.biblioteczka.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.DialogAddBookBinding
import com.example.biblioteczka.databinding.DialogSendBinding
import com.example.biblioteczka.model.Book

class SendDialogFragment(private val viewModel: HomeViewModel): DialogFragment() {


    init {


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: DialogSendBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_send, container, false)


        return binding.root
    }




    companion object {
        const val TAG = "SendDialog"
    }
}