package com.ken.cinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.ken.cinema.data.model.Film
import com.ken.cinema.data.repository.FakeMovieDBApi
import com.ken.cinema.data.repository.FilmFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MoviePagingSourceTest {

    private val filmFactory = FilmFactory()
    private val fakeMovies = listOf(
        filmFactory.createFilm("spiderman"),
        filmFactory.createFilm("nemesis"),
        filmFactory.createFilm("superman"),
        filmFactory.createFilm("spider")
    )

    private val fakeApi = FakeMovieDBApi().apply {
        fakeMovies.forEach { movie -> addMovie(movie) }
    }


    @Test
    fun pageKeyedMoviePagingSource() = runTest {
        val pagingSource = MoviePagingSource(fakeApi, "")
        assertEquals<PagingSource.LoadResult<out Int, Film>>(
            expected = Page (
                data = listOf(fakeMovies[0], fakeMovies[1]),
                prevKey = null,
                nextKey = 2
            ),
            actual = pagingSource.load(
                Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    fun getMovie_stringQuery_returnsTwoMovies() = runTest {
        //Given a paging source with a fakeApi and query
        val pagingSource = MoviePagingSource(fakeApi, "spider")

        // When a query is run by Paging source through the fake api
        // Then data returned should match fakeMovies list first and last items
        assertEquals<PagingSource.LoadResult<out Int, Film>>(
            expected = Page (
                data = listOf(fakeMovies[0], fakeMovies.last()),
                prevKey = null,
                nextKey = 2
            ),
            actual = pagingSource.load(
                Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ),
        )
    }
}