package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFilmUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorEventUseCase<String, Film>() {

    override fun execute(parameters: String) {

        val source = filmRepository.getFilm(parameters)
        result.removeSource(source)
        result.addSource(source) { resource ->
            result.postValue(resource)
        }
    }
}