package com.example.biblioteczka.ui.home


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteczka.R
import com.example.biblioteczka.databinding.BookItemBinding
import com.example.biblioteczka.model.Book

class BookListAdapter : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    var buttonClickListener: BookRecyclerViewClickListener? = null
    private var items: List<Book> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(books: List<Book>) {
        items = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = DataBindingUtil.inflate<BookItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.book_item,
            parent,
            false
        )
        return BookViewHolder(binding, buttonClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            buttonClickListener?.onBookItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size


    class BookViewHolder(private val binding: BookItemBinding, var buttonClickListener: BookRecyclerViewClickListener?) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.hireButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener?.onHireButtonClick(position)
                }
            }

            binding.shareButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener?.onShareButtonClick(position)
                }
            }

            binding.returnButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener?.onReturnButtonClick(position)
                }
            }

            binding.acceptButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener?.onAcceptButtonClick(position)
                }
            }

        }

        fun bind(item: Book) {
            binding.book= item
            binding.site.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener?.onSiteClick(item)
                }
            }
            binding.executePendingBindings()
        }
    }

}