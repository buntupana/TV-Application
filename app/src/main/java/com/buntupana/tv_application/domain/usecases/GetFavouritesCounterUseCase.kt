package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFavouritesCounterUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorUseCase<Unit, Int>() {
    override fun execute(parameters: Unit) {
        val source = filmRepository.getFavouritesCount()
        result.removeSource(source)
        result.addSource(source) { resource ->
            result.postValue(resource)
        }
    }
}