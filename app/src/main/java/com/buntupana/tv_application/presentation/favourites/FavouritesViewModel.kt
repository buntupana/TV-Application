package com.buntupana.tv_application.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.usecases.GetFavouritesFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouritesFilmUseCase: GetFavouritesFilmUseCase
) : ViewModel() {

    val favouriteFilmListLiveData: LiveData<List<Film>> = getFavouritesFilmUseCase(Unit)
}