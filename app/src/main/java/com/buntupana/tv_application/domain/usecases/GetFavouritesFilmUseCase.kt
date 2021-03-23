package com.buntupana.tv_application.domain.usecases

import androidx.lifecycle.LiveData
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFavouritesFilmUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : UseCase<Unit, LiveData<List<Film>>>() {

    override fun execute(parameters: Unit): LiveData<List<Film>> {
        return filmRepository.getFilmsFavourites()
    }
}