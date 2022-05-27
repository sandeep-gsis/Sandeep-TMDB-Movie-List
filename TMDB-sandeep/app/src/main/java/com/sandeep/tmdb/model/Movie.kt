package com.sandeep.tmdb.model

data class Movie(    val id: Int,
                     val title: String? = "",
                     val overview: String? = "",
                     val releaseDate: String? = "",
                     val backdropPath: String? = "",
                     val posterPath: String? = "",
                     val voteAverage: Double? = 0.0,
                     val voteCount: Int? = 0
)