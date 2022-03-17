 package com.ken.cinema.data

import androidx.paging.*
import com.ken.cinema.BuildConfig
import com.ken.cinema.data.model.Film
import com.ken.cinema.data.network.ApiClient
import retrofit2.HttpException
import java.io.IOException


private const val MOVIEDB_STARTING_PAGE_INDEX = 1

class MoviePagingSource(
    private val service: ApiClient,
    private val query: String?
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {

        return try {

            val position = params.key ?: MOVIEDB_STARTING_PAGE_INDEX

            val response = if(query.isNullOrEmpty()) service.getMoviesPopular(BuildConfig.apiKey, position) else
                service.getMovie(BuildConfig.apiKey,position,query)

            return LoadResult.Page(
                data = response.results,
                prevKey = if (position == MOVIEDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.results.isNullOrEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}