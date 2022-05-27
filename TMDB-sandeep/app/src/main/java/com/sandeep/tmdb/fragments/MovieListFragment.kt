package com.sandeep.tmdb.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sandeep.tmdb.viewModels.MovieListViewModel
import com.sandeep.tmdb.R
import com.sandeep.tmdb.adapter.MovieListAdapter
import kotlinx.android.synthetic.main.fragment_movie_list.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandeep.tmdb.utils.gone
import com.sandeep.tmdb.utils.visible
import kotlinx.android.synthetic.main.fragment_movie_list.progressBar
import kotlinx.android.synthetic.main.fragment_movie_list.swipeRefreshList
import kotlinx.android.synthetic.main.fragment_movie_list.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieListFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_movie_list
    private val viewModel: MovieListViewModel by  activityViewModels()

    private var movieListAdapter = MovieListAdapter().setItemListener { movie->
            viewModel.selectedMovie.value = movie

            if (findNavController().currentDestination?.id == R.id.movieListFragment) {
                val directions = MovieListFragmentDirections.toDetailsFragment()
                findNavController().navigate(directions)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            activity?.invalidateOptionsMenu()
            setHasOptionsMenu(true)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupRecyclerView()
            initViews()
            viewModel.loadMovieList()

            viewModel.movieList.observe(viewLifecycleOwner, Observer { list ->
                progressBar.gone()
                if (list.isNotEmpty()) {
                    movieListAdapter.setList(list)
                }
            })
            viewModel.error.observe(viewLifecycleOwner, Observer { message ->
                progressBar.gone()
                tvError.text =  message
            })
        }

    private fun setupRecyclerView() {
        progressBar.visible()
        activity?.let {
            rvMovies.apply {
                layoutManager = GridLayoutManager(it, 2)
                adapter = movieListAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val count = (layoutManager as GridLayoutManager).itemCount
                        val lastPos =
                            (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                        if (lastPos == count - 1 && dy > 0) {
                            viewModel.loadMovieList()
                       }
                    }
                })
            }
        }
    }
     private fun initViews() {
            swipeRefreshList.apply {
                setOnRefreshListener {
                    swipeRefreshList.isRefreshing = false
                    viewModel.currentPage = 1
                    viewModel.loadMovieList()
                }
            }
        }

    }