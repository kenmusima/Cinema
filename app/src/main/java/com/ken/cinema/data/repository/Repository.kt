package com.ken.cinema.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ken.cinema.data.MoviePagingSource
import com.ken.cinema.data.db.dao.TicketDao
import com.ken.cinema.data.db.entity.Ticket
import com.ken.cinema.data.model.Film
import com.ken.cinema.data.network.ApiClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiClient: ApiClient,
    private val dao: TicketDao
) : DataSourceRepository {


    override fun getMovies() : Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders =false
            ),
            pagingSourceFactory = {MoviePagingSource(apiClient,"")}
        ).flow
    }

    override fun getMovie(query: String) : Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders =false
            ),
            pagingSourceFactory = {MoviePagingSource(apiClient,query)}
        ).flow
    }

    override suspend fun saveTicket(ticket: Ticket) = dao.insert(ticket)

    override fun getTickets() : Flow<List<Ticket>> = dao.getAllTickets()
}