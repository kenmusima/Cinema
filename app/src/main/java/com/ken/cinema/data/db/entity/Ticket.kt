package com.ken.cinema.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class Ticket(
    val seatNumbers: Int,
    val filmTitle: String,
    val runDate: Long?,
    val price: Int,
    val image: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
