package com.buntupana.tv_application.core

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.buntupana.tv_application.R

@BindingAdapter("showDivider")
fun showDivider(recyclerView: RecyclerView?, show: Boolean) {
    if (show) {
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                LinearLayout.VERTICAL
            )
        )
    }
}

@BindingAdapter("android:text")
fun setStringResToTextView(textView: TextView, @StringRes resource: Int) {
    if (resource != 0) textView.setText(resource)
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(ColorDrawable(ContextCompat.getColor(view.context, R.color.icon_default)))
            .apply(requestOptions)
            .into(view)
    }
}