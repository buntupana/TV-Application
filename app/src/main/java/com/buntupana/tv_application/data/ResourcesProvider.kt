package com.buntupana.tv_application.data

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

/** Provide string resources**/
class ResourcesProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }
}