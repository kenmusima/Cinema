package com.ken.cinema.data.repository

import com.ken.cinema.data.model.Film
import com.ken.cinema.data.network.ApiClient
import com.ken.cinema.data.network.NetworkResponse
import java.io.IOException
import kotlin.math.min

class FakeMovieDBApi : ApiClient {

    // movies paged by name
    private val model = mutableMapOf<String, Movies>()
    var failureMsg: String? = null


    fun addMovie(film: Film) {
        val movies = model.getOrPut(film.title) {
            Movies(items = arrayListOf())
        }
        movies.items.add(film)
    }

    private fun findMoviesPopular() = model.values

    private fun findMovieMatchingQuery(movie: String) =
        model.filter { entry -> entry.key.startsWith(movie) }.values


    override suspend fun getMoviesPopular(api_key: String, page: Int): NetworkResponse {
        if (api_key == null) {
            failureMsg?.let {
                throw IOException(it)
            }
        }

        val item = findMoviesPopular().flatMap { it.items }

        return NetworkResponse(results = Movies(items = item.toMutableList()).findMovies(page))
    }

    override suspend fun getMovie(api_key: String, page: Int, query: String): NetworkResponse {
        if (api_key == null) {
            failureMsg?.let {
                throw IOException(it)
            }
        }

        if (query.isNullOrEmpty()){
            return NetworkResponse(emptyList())
        }

        val movie = findMovieMatchingQuery(query).flatMap { it.items }

        return NetworkResponse(results = Movies(items = movie.toMutableList()).findMovies(page))
    }

    private class Movies(val items: MutableList<Film> = arrayListOf()) {
        fun findMovies(page: Int): List<Film> {
            return if (page == 1){
                items.subList(0, min(items.size,2))
            } else {
                items.subList(2,items.size)
            }
        }
    }

}