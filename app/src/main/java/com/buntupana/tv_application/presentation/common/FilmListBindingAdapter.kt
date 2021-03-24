package com.buntupana.tv_application.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buntupana.tv_application.databinding.ItemFilmBinding

class FilmListBindingAdapter :
    ListAdapter<FilmEntityView, FilmListBindingAdapter.BindingHolder>(SettDiffCallback<FilmEntityView>()) {

    var listener: OnFilmItemClickListener? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bind()
    }

    inner class BindingHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.imageFavourite.setOnClickListener { onFavouriteClick() }
        }

        fun bind() {
            binding.item = getItem(layoutPosition)
            binding.executePendingBindings()
        }

        override fun onClick(view: View) {
            listener?.onItemClick(binding, adapterPosition)
        }

        private fun onFavouriteClick() {
            val item = getItem(layoutPosition)
            listener?.onFavouriteClick(!item.favourite, layoutPosition)
        }
    }

    interface OnFilmItemClickListener {
        fun onItemClick(binding: ItemFilmBinding, position: Int)
        fun onFavouriteClick(favorite: Boolean, position: Int)
    }
}