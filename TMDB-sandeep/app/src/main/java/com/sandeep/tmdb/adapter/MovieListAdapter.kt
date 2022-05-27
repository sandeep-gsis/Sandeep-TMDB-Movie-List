package com.sandeep.tmdb.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandeep.tmdb.R
import com.sandeep.tmdb.model.Movie
import com.sandeep.tmdb.utils.inflateView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_movie.view.*
import com.sandeep.tmdb.utils.formattedDate


class MovieListAdapter : BaseAdapter<MovieListAdapter, Movie, MovieListAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = parent.inflateView(R.layout.item_movie, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val item = itemList[position]

        holder.itemView.tvMovieTitle.text = item.title
        holder.itemView. tvMovieReleaseDate.text = formattedDate(item.releaseDate ?: "")
        if(!item.posterPath.isNullOrEmpty()){
            Glide.with(holder.itemView.ivMovie)
                .load(item.posterPath)
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.itemView.ivMovie)
        }
        holder.itemView.setOnClickListener { itemClickListener(item) }
    }

    class AdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
