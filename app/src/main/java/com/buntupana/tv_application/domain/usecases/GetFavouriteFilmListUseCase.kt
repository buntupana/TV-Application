package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetFavouriteFilmListUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorUseCase<String, List<Film>>() {

    private var _filmList = listOf<Film>()
    private var searchKey = ""

    override fun execute(parameters: String) {

        // We save the searchKey so when the film table is updated we can filter
        searchKey = parameters

        // When searchKey is blank we'll launch a network call
        if (searchKey.isBlank()) {
            val source = filmRepository.getFilmsFavourites()
            result.removeSource(source)
            result.addSource(source) { filmList ->
                _filmList = filmList
                // it will post the filter list with the given string when table is uploaded
                result.postValue(Resource.Success(filterCustomers()))

            }
        } else {
            // when we already have a searchKey it will filter with the list we already
            result.postValue(Resource.Success(filterCustomers()))
        }
    }

    /**
     * Filter the film list with the search key, it will return a list of films that have
     * in their title the given search key
     * @return a customer list
     */
    private fun filterCustomers(): List<Film> {
        return _filmList.filter { customer ->
            customer.title.contains(searchKey, true)
        }.sortedBy { it.title }
    }
}