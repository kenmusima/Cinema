package com.ken.cinema.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ken.cinema.R
import com.ken.cinema.data.db.entity.Ticket
import com.ken.cinema.databinding.ListItemTicketBinding
import com.ken.cinema.util.IMAGE_PREFIX_URL
import com.ken.cinema.util.convertLongToTime

class OrderAdapter : ListAdapter<Ticket, OrderAdapter.OrderViewHolder>(DiffUtilCallback) {


    class OrderViewHolder(val binding: ListItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: Ticket) {

            binding.filmTitle.text = "Title: ${ticket.filmTitle}"
            binding.numberOfSeats.text = "Number of seats: ${ticket.seatNumbers.toString()}"
            binding.ticketPrice.text = "Price: ${ticket.price.toString()}"
            binding.dateBooked.text = "Date: ${ticket.runDate?.convertLongToTime()}"

            Glide.with(binding.root)
                .load("${IMAGE_PREFIX_URL.plus(ticket.image)}")
                .centerCrop()
                .placeholder(R.drawable.loading_animation)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.filmImage)
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemTicketBinding =
            ListItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(itemTicketBinding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val ticket = getItem(position)
        holder.bind(ticket)
    }
}