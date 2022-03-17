package com.ken.cinema.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ken.cinema.data.model.Film
import com.ken.cinema.databinding.FragmentMovieDetailsBinding
import com.ken.cinema.util.IMAGE_PREFIX_URL
import com.ken.cinema.util.simpleDateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null

    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        film = args.film

        Glide.with(this)
            .load(IMAGE_PREFIX_URL.plus(film.poster_path))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.movieImage)
        binding.title.text = film.title
        binding.overview.text = film.overview
        binding.voteAverage.text = film.vote_average.toString()
        binding.releaseDate.text = film.release_date.simpleDateFormat()

        binding.selectMovie.setOnClickListener {
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToSeatFragment(film)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}