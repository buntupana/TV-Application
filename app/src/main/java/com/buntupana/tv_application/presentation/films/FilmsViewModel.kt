package com.buntupana.tv_application.presentation.films

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.usecases.GetFilmListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    private val getFilmListUseCase: GetFilmListUseCase
) : ViewModel() {

    val filmListLiveData: LiveData<Resource<List<Film>>> = getFilmListUseCase.observe()

    init {
        getFilmListUseCase(Unit)
    }

}