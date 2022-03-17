package com.ken.cinema.data.network

import androidx.lifecycle.LiveData
import com.ken.cinema.data.model.Film
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("movie/popular")
    suspend fun getMoviesPopular(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ) : NetworkResponse


    @GET("search/movie")
    suspend fun getMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ) : NetworkResponse


}