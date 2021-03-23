package com.buntupana.tv_application.domain.usecases

import androidx.lifecycle.LiveData
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFavouritesCounterUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : UseCase<Unit, LiveData<Int>>() {
    override fun execute(parameters: Unit) : LiveData<Int> {
        return filmRepository.getFavouritesCount()
    }
}