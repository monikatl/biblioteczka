package com.example.biblioteczka.ui.notifications


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.PersonItemBinding
import com.example.biblioteczka.databinding.RentalItemBinding
import com.example.biblioteczka.model.Book
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.model.Rental
import java.time.format.DateTimeFormatter

class RentalListAdapter(private var items: List<Rental>) : RecyclerView.Adapter<RentalListAdapter.ViewHolder>() {

    fun submitList(rentals: List<Rental>) {
        items = rentals
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RentalItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rental_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(private val binding: RentalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Rental) {
            binding.rental = item

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val takeDate = item.take_date?.format(formatter) ?: "-"
            val planReturnDate = item.plan_return_date?.format(formatter) ?: "-"
            val returnDate = item.return_date?.format(formatter) ?: "-"

            binding.planReturnDate.text = "Planowana data zwrotu: $planReturnDate"
            binding.takeDate.text = "Data wypożyczenia: $takeDate"
            binding.returnDate.text = "Data zwrotu: $returnDate"

            binding.executePendingBindings()
        }
    }
}
