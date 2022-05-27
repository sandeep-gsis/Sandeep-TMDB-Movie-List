package com.sandeep.tmdb.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sandeep.tmdb.BuildConfig
import com.sandeep.tmdb.model.Movie
import com.sandeep.tmdb.model.ResponseMoviesList
import com.sandeep.tmdb.model.checkResultSusp
import com.sandeep.tmdb.model.result
import com.sandeep.tmdb.network.DataProvider
import kotlinx.coroutines.launch

class MovieListViewModel : BaseViewModel() {

    val selectedMovie = MutableLiveData<Movie>()


    private val _movieActualList = MutableLiveData<ResponseMoviesList>()

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var currentPage = 1
    private var totalPages = 1

    fun loadMovieList() {
        launch {
            if (currentPage <= totalPages) {
                DataProvider.getMoviesList(
                    currentPage,
                    BuildConfig.API_KEY
                ).checkResultSusp(
                    success = { response ->
                        _movieActualList.value = response
                        totalPages = response.totalPages
                        currentPage++
                        _movieList.value = response.results.map { mapToMovie(it) }
                    },
                    error = {
                        _error.value = it.errorResponse.message.toString()
                        Log.d("error", it.errorResponse.message.toString())
                        it.printStackTrace()
                    })
            }
        }
    }

    private fun mapToMovie(data: result) : Movie {
        return with(data){
            Movie(
                id = this.id,
                title = this.title,
                overview = this.overview,
                backdropPath = BuildConfig.BACKDROP_IMAGES_BASEURL + this.backdropPath,
                posterPath = BuildConfig.POSTER_IMAGES_BASEURL + this.posterPath,
                releaseDate = this.releaseDate,
                voteAverage = this.voteAverage,
                voteCount = this.voteCount
            )
        }
    }
}

