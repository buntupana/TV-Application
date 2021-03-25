package com.buntupana.tv_application.presentation.common

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
import com.buntupana.tv_application.presentation.films.FilmEntityView

/**
 * ListAdapter for [R.layout.item_film] and [R.layout.item_film_favorite]
 */
class FilmListBindingAdapter<BINDING : ViewDataBinding>(
    @LayoutRes
    private var layoutRes: Int
) : ListAdapter<FilmEntityView, FilmListBindingAdapter<BINDING>.BindingHolder>(SettDiffCallback<FilmEntityView>()) {

    var listenerFilm: OnFilmItemClickListener? = null
    var listenerFavourite: OnFavouriteFilmItemClickListener? = null

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
            if (binding is ItemFilmBinding) {
                listenerFilm?.onItemClick(binding, adapterPosition)
            } else if (binding is ItemFilmFavoriteBinding) {
                listenerFavourite?.onItemClick(binding, adapterPosition)
            }
        }

        private fun onFavouriteClick() {
            val item = getItem(layoutPosition)
            if (binding is ItemFilmBinding) {
                listenerFilm?.onFavouriteClick(!item.favourite, layoutPosition)
            } else if (binding is ItemFilmFavoriteBinding) {
                listenerFavourite?.onFavouriteClick(!item.favourite, layoutPosition)
            }
        }
    }

    interface OnFilmItemClickListener {
        fun onItemClick(binding: ItemFilmBinding, position: Int)
        fun onFavouriteClick(favorite: Boolean, position: Int)
    }

    interface OnFavouriteFilmItemClickListener {
        fun onItemClick(binding: ItemFilmFavoriteBinding, position: Int)
        fun onFavouriteClick(favorite: Boolean, position: Int)
    }
}