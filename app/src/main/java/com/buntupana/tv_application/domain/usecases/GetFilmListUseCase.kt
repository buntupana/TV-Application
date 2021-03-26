package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.repositories.FilmRepository
import timber.log.Timber
import javax.inject.Inject

class GetFilmListUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorUseCase<String, List<Film>>() {

    private var filmList = listOf<Film>()
    private var searchKey = ""

    override fun execute(parameters: String) {

        // We save the searchKey so when the film table is updated we can filter
        searchKey = parameters

        // When searchKey is blank we'll launch a network call
        if (searchKey.isBlank()) {
            result.removeSource(source)
            source = filmRepository.getFilmList()
            result.addSource(source) { resource ->
                when (resource) {
                    is Resource.Error -> {
                        result.postValue(Resource.Error(resource.exception))
                    }
                    is Resource.Loading -> result.postValue(Resource.Loading())
                    is Resource.Success -> {
                        filmList = resource.data ?: listOf()
                        // it will post the filter list with the given string when table is uploaded
                        result.postValue(Resource.Success(filterFilms()))
                    }
                }
            }
        } else {
            // when we already have a searchKey it will filter with the list we already
            result.postValue(Resource.Success(filterFilms()))
        }
    }

    /**
     * Filter the film list with the search key, it will return a list of films that have
     * in their title the given search key
     * @return a customer list
     */
    private fun filterFilms(): List<Film> {
        Timber.d("filterCustomers() called")
        return filmList.filter { customer ->
            customer.title.contains(searchKey, true)
        }
    }
}