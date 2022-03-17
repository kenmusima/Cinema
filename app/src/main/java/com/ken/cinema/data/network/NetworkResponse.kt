package com.ken.cinema.data.network

import com.ken.cinema.data.model.Film


data class NetworkResponse (
    val results: List<Film>
)