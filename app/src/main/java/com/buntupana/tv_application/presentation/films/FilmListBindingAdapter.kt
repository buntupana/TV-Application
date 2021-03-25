package com.buntupana.tv_application.presentation.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buntupana.tv_application.R
import com.buntupana.tv_application.databinding.ItemFilmBinding
import com.buntupana.tv_application.databinding.ItemFilmFavoriteBinding
import com.buntupana.tv_application.presentation.common.SettDiffCallback

/**
 * ListAdapter for [R.layout.item_film] and [R.layout.item_film_favorite]
 */
class FilmListBindingAdapter<BINDING : ViewDataBinding>(
    @LayoutRes
    private var layoutRes: Int
) : ListAdapter<FilmEntityView, FilmListBindingAdapter<BINDING>.BindingHolder>(SettDiffCallback<FilmEntityView>()) {

    var listenerFilm: OnFilmItemClickListener? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<BINDING>(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bind()
    }

    inner class BindingHolder(private val binding: BINDING) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            if (binding is ItemFilmBinding) {
                binding.imageFavourite.setOnClickListener { onFavouriteClick() }
            } else if (binding is ItemFilmFavoriteBinding) {
                binding.imageFavourite.setOnClickListener { onFavouriteClick() }
            }
        }

        fun bind() {
            if (binding is ItemFilmBinding) {
                binding.item = getItem(layoutPosition)
            } else if (binding is ItemFilmFavoriteBinding) {
                binding.item = getItem(layoutPosition)
            }
            binding.executePendingBindings()
        }

        override fun onClick(view: View) {
            val filmId = getItem(layoutPosition).id
            listenerFilm?.onItemClick(filmId)
        }

        private fun onFavouriteClick() {
            val item = getItem(layoutPosition)
            listenerFilm?.onFavouriteClick(!item.favourite, layoutPosition)
        }
    }

    interface OnFilmItemClickListener {
        fun onItemClick(filmId: String)
        fun onFavouriteClick(favorite: Boolean, position: Int)
    }
}