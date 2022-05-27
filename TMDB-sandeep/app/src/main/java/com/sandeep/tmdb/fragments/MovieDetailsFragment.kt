package com.sandeep.tmdb.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sandeep.tmdb.viewModels.MovieListViewModel
import com.sandeep.tmdb.R
import com.sandeep.tmdb.utils.formattedDate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.fragment_movie_details.*
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetailsFragment :  BaseFragment() {
    override val layoutId: Int = R.layout.fragment_movie_details
    private val viewModel: MovieListViewModel by  activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        detailToolbar.setOnClickListener {
            findNavController().popBackStack() }
    }

    private fun initObservers() {
        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            it?.let { movie ->
                tvMovieTitle.text = movie.title
                tvMovieOverview.text = movie.overview
                tvVoteAvg.text = resources.getString(R.string.vote_average, movie.voteAverage)
                tvVoteCount.text = resources.getString(R.string.vote_count, movie.voteCount)
                tvReleaseDate.text = resources.getString(R.string.release_date, formattedDate(movie.releaseDate ?: ""))

                if(!movie.posterPath.isNullOrEmpty()){
                    Glide.with(ivMoviePoster)
                        .load(movie.posterPath)
                        .placeholder(R.drawable.ic_launcher_background)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivMoviePoster)
                }

                if(!movie.backdropPath.isNullOrEmpty()){
                    Glide.with(ivBackdrop)
                        .load(movie.backdropPath)
                        .placeholder(R.drawable.ic_launcher_background)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivBackdrop)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}