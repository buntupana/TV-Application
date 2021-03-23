package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFilmListUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorEventUseCase<Unit, List<Film>>() {

    override fun execute(parameters: Unit) {

        val source = filmRepository.getFilmList()
        result.removeSource(source)
        result.addSource(source) { resource ->
            result.postValue(resource)
        }
    }
}