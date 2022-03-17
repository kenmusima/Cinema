package com.ken.cinema.data.repository

import androidx.paging.PagingData
import com.ken.cinema.data.db.entity.Ticket
import com.ken.cinema.data.model.Film
import kotlinx.coroutines.flow.Flow

interface DataSourceRepository {
    fun getMovies() : Flow<PagingData<Film>>
    fun getMovie(query: String) : Flow<PagingData<Film>>

    suspend fun saveTicket(ticket: Ticket)
    fun getTickets() : Flow<List<Ticket>>
}