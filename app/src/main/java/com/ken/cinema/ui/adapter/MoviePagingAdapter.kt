package com.ken.cinema.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ken.cinema.R
import com.ken.cinema.data.model.Film
import com.ken.cinema.databinding.ListItemMovieBinding
import com.ken.cinema.ui.fragment.MainFragmentDirections
import com.ken.cinema.util.IMAGE_PREFIX_URL

class MoviePagingAdapter :
    PagingDataAdapter<Film, MoviePagingAdapter.MovieViewHolder>(MovieDiffCallback) {


    object MovieDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }

    }

    class MovieViewHolder(private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.movieItemName.text = film.title
            Glide.with(binding.root)
                .load("${IMAGE_PREFIX_URL.plus(film.poster_path)}")
                .placeholder(R.drawable.loading_animation)
                .fallback(R.drawable.ic_baseline_movie_24)
                .error(R.drawable.ic_baseline_movie_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.movieItemImage)

            binding.card.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToMovieDetailFragment(film)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

}