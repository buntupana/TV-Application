package com.buntupana.tv_application.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buntupana.tv_application.domain.usecases.GetFavouritesCounterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getFavouritesCounterUseCase: GetFavouritesCounterUseCase
) : ViewModel() {

    val favouritesCountObservable: LiveData<Int> = getFavouritesCounterUseCase(Unit)
}