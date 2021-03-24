package com.buntupana.tv_application.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buntupana.tv_application.R

abstract class FilmsGenericViewModel : ViewModel() {

    /** Info message to show when there is no results */
    private val _infoMessage = MutableLiveData(R.string.message_no_matches)
    val infoMessage: LiveData<Int>
        get() = _infoMessage

    fun setInfoAsNoMatchesFound() {
        _infoMessage.value = R.string.message_no_matches
    }

    fun setInfoNetWorkProblem() {
        _infoMessage.value = R.string.message_error_connection
    }

    abstract fun browse(searchKey: String)
}

data class FilmEntityView(
    val id: String,
    val imageSrc: String,
    val title: String,
    val year: Int,
    val durationHours: Int,
    val durationMin: Int,
    val categories: String,
    val favourite: Boolean
)
