package com.buntupana.tv_application.presentation.films

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.usecases.GetFilmListUseCase
import com.buntupana.tv_application.domain.usecases.SetFavouriteParameters
import com.buntupana.tv_application.domain.usecases.SetFavouriteUseCase
import com.buntupana.tv_application.presentation.common.FilmEntityView
import com.buntupana.tv_application.presentation.common.FilmsGenericViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    private val getFilmListUseCase: GetFilmListUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase
) : FilmsGenericViewModel() {

    private var filmList: List<Film> = listOf()
    val filmViewEntityList: LiveData<Resource<List<FilmEntityView>>>
    private var _searchKey = ""

    init {

        filmViewEntityList = getFilmListUseCase.observe().map { resource ->
            when (resource) {
                is Resource.Error -> {
                    Resource.Error(resource.exception)
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
                is Resource.Success -> {
                    filmList = resource.data ?: listOf()
                    resource.data!!.map { film ->
                        FilmEntityViewMapper().apply(film)
                    }.let { filmEntityViewList ->
                        Resource.Success(filmEntityViewList)
                    }
                }
            }
        }

        browse(_searchKey)
    }

    override fun browse(searchKey: String) {
        _searchKey = searchKey
        getFilmListUseCase.execute(_searchKey)
    }

    fun setFavourite(favourite: Boolean, position: Int) {
        filmList[position].let { film ->
            setFavouriteUseCase(SetFavouriteParameters(film.filmId, favourite))
        }
    }

    class FilmEntityViewMapper : Function<Film, FilmEntityView> {
        override fun apply(input: Film): FilmEntityView {

            var durationMin: Int = (input.duration / 1000 / 60).toInt()
            val durationHours: Int = durationMin / 60
            do {
                durationMin -= 60
            } while (durationMin >= 60)
            var categories = ""
            input.categoryList.forEachIndexed { index, category ->
                categories += if (index == 0) {
                    category.name
                } else {
                    ", ${category.name}"
                }
            }

            return FilmEntityView(
                input.filmId,
                input.coverResource,
                input.title,
                input.year,
                durationHours,
                durationMin,
                categories,
                input.favourite
            )
        }
    }
}