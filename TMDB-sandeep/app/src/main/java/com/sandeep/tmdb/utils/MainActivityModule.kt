package com.sandeep.tmdb.utils

import com.sandeep.tmdb.viewModels.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object MainActivityModule {

    val module = module {
        viewModel { MovieListViewModel() }
    }
}