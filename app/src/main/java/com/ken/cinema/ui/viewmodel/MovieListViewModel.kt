package com.ken.cinema.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ken.cinema.data.model.Film
import com.ken.cinema.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun fetchMovies(): Flow<PagingData<Film>> =
        repository.getMovies().cachedIn(viewModelScope)

    fun searchMovie(query: String): Flow<PagingData<Film>> =
        repository.getMovie(query).cachedIn(viewModelScope)
}