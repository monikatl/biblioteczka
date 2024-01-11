package com.example.biblioteczka.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.PersonItemBinding
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person

class PersonListAdapter(private val action: (Person) -> Unit) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {

    private var items: List<Person> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(persons: List<Person>) {
        items = persons
        notifyDataSetChanged()
    }

    fun filterList(filterList: List<Person>) {
        items = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<PersonItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.person_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = items[position]
        holder.bind(person)
        holder.itemView.setOnClickListener { action.invoke(person) }
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(private val binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Person) {
            binding.person = item
            binding.executePendingBindings()
        }
    }
}
