package com.buntupana.tv_application.presentation.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class SettDiffCallback<ITEM> : DiffUtil.ItemCallback<ITEM>() {
    override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
        return oldItem == newItem
    }
}