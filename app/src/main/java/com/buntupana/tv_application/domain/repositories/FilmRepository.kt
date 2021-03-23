package com.buntupana.tv_application.domain.repositories

import androidx.lifecycle.LiveData
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Recommendation
import com.buntupana.tv_application.domain.entities.Resource

interface FilmRepository {

    fun getFilm(filmId: String): LiveData<Resource<Film>>

    fun getFilmList(): LiveData<Resource<List<Film>>>

    fun setFavourite(filmId: String, favourite: Boolean)

    fun getRecommendationList(filmId: String): LiveData<Resource<List<Recommendation>>>

    fun getFavouritesCount(): LiveData<Int>

    fun getFilmsFavourites(): LiveData<List<Film>>
}