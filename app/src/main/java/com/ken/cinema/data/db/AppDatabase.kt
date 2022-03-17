package com.ken.cinema.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ken.cinema.data.db.dao.TicketDao
import com.ken.cinema.data.db.entity.Ticket

@Database(entities = [Ticket::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase  : RoomDatabase() {
    abstract fun ticketDao() : TicketDao
}