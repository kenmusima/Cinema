package com.ken.cinema.data.repository

import com.ken.cinema.data.model.Film
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

class FilmFactory {
    private val counter = AtomicInteger(0)
    fun createFilm(filmName: String): Film {
        val id = counter.incrementAndGet()
        return Film(
            id = id,
            title = filmName,
            backdrop_path = "backdrop_path $id",
            adult = false,
            overview = "overview $id",
            popularity = 10.0,
            poster_path = "poster_path $id",
            release_date = "${LocalDate.now()}",
            vote_average = 8.0,
            vote_count = 5
        )
    }
}