package com.buntupana.tv_application.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buntupana.tv_application.BR
import timber.log.Timber

/**
 * Generic RecyclerView.Adapter for a list of ITEMS and a BINDING class
 */
class SimpleListBindingAdapter<ITEM : DefaultItem, BINDING : ViewDataBinding>(
    @LayoutRes
    private var layoutRes: Int
) :
    ListAdapter<ITEM, SimpleListBindingAdapter<ITEM, BINDING>.BindingHolder>(SettDiffCallback<ITEM>()) {

    var listener: OnItemClickListener<BINDING>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {

        val binding = DataBindingUtil.inflate<BINDING>(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )

        if (binding == null) {
            Timber.e("Layout resource given is not wrapped with <layout> tag")
        }
        return BindingHolder(binding!!)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bind()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    inner class BindingHolder(private val binding: BINDING) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind() {
            binding.setVariable(BR.item, getItem(layoutPosition))
            binding.executePendingBindings()
        }

        override fun onClick(view: View) {
            listener?.onItemClick(binding, adapterPosition)
        }
    }

    interface OnItemClickListener<BINDING : ViewDataBinding> {
        fun onItemClick(binding: BINDING, position: Int)
    }
}
