package com.ken.cinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ken.cinema.data.db.entity.Ticket
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: Ticket)

    @Query("SELECT * FROM tickets")
    fun getAllTickets(): Flow<List<Ticket>>


    @Query("DELETE FROM tickets")
    suspend fun clearMovies()
}