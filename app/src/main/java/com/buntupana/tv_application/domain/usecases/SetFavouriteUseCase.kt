package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class SetFavouriteUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : UseCase<SetFavouriteParameters, Unit>() {
    override fun execute(parameters: SetFavouriteParameters) {
        filmRepository.setFavourite(parameters.filmId, parameters.favourite)
    }
}

class SetFavouriteParameters(val filmId: String, val favourite: Boolean)