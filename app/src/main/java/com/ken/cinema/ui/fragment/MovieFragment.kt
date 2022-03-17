package com.ken.cinema.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ken.cinema.BuildConfig
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentMovieBinding
import com.ken.cinema.ui.adapter.MovieLoadStateAdapter
import com.ken.cinema.ui.adapter.MoviePagingAdapter
import com.ken.cinema.ui.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie) {

    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!
    private val viewModel by viewModels<MovieListViewModel>()

    private val pagingAdapter by lazy { MoviePagingAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBinding.bind(view)

        refreshList()
        initRecyclerView()
        loadMovies()
        scrollSetUp()
        searchMovies()
        binding.scrollIndicatorUp.setOnClickListener {
            lifecycleScope.launch {
                binding.recyclerViewMovies.scrollToPosition(0)
                delay(100)
            }
        }



    }

    private fun scrollSetUp() {
        binding.recyclerViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrollPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (scrollPosition != null) {
                    if (scrollPosition >= 1) {
                        binding.scrollIndicatorUp.visibility = View.VISIBLE
                    } else {
                        binding.scrollIndicatorUp.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun refreshList() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.search.setQuery("",false)
            binding.search.clearFocus()
            loadMovies()

        }
    }

    private fun initRecyclerView() {
        val footer = MovieLoadStateAdapter { pagingAdapter.retry() }

        val gridLayoutManager = GridLayoutManager(requireContext(),2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == pagingAdapter.itemCount && footer.itemCount > 0 ) 2
                else 1
            }
        }

        binding.recyclerViewMovies.apply {
            layoutManager= gridLayoutManager
            setHasFixedSize(true)
        }
        binding.recyclerViewMovies.adapter = pagingAdapter.withLoadStateFooter(footer = footer)

        pagingAdapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh is LoadState.Loading && pagingAdapter.snapshot().isEmpty()) {
                binding.reload.isVisible = true
            } else {
                binding.reload.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
                    loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
                    loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
                    else -> null
                }

                if (pagingAdapter.snapshot().isEmpty()) {
                    error?.let {
                        binding.retry.visibility = View.VISIBLE
                        binding.retry.setOnClickListener {
                            pagingAdapter.retry()
                            binding.retry.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun loadMovies() {
        lifecycleScope.launch {
            viewModel.fetchMovies().collectLatest {  pagingAdapter.submitData(it) }
        }
    }

    private fun searchMovies() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        viewModel.searchMovie(query).collectLatest {
                            pagingAdapter.submitData(it)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}