package com.buntupana.tv_application.presentation.films

import androidx.lifecycle.*
import com.buntupana.tv_application.R
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.usecases.GetFavouriteFilmListUseCase
import com.buntupana.tv_application.domain.usecases.GetFilmListUseCase
import com.buntupana.tv_application.domain.usecases.SetFavouriteParameters
import com.buntupana.tv_application.domain.usecases.SetFavouriteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class FilmsViewModel @AssistedInject constructor(
    private val getFilmListUseCase: GetFilmListUseCase,
    private val getFavouriteFilmListUseCase: GetFavouriteFilmListUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase,
    @Assisted private val typeScreen: TypeScreen
) : ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(typeScreen: TypeScreen): FilmsViewModel
    }

    enum class TypeScreen { FILMS, FAVOURITES }

    var searchKey = ""
    private var _filmList: List<Film> = listOf()

    val filmViewEntityList: LiveData<Resource<List<FilmEntityView>>>

    /** Info message to show when there is no results */
    private val _infoMessage = MutableLiveData(R.string.message_no_matches)
    val infoMessage: LiveData<Int>
        get() = _infoMessage

    init {
        filmViewEntityList = when (typeScreen) {
            TypeScreen.FILMS -> getFilmListUseCase.observe().map { getResource(it) }
            TypeScreen.FAVOURITES -> getFavouriteFilmListUseCase.observe().map { getResource(it) }
        }
        executeBrowse()
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            typeScreen: TypeScreen
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(typeScreen) as T
            }

        }
    }

    private fun getResource(resource: Resource<List<Film>>): Resource<List<FilmEntityView>> {
        return when (resource) {
            is Resource.Error -> {
                Resource.Error(resource.exception)
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
            is Resource.Success -> {
                _filmList = resource.data ?: listOf()
                // mapping film to filmEntityView
                resource.data!!.map { film ->
                    val imageResource = when (typeScreen) {
                        TypeScreen.FILMS -> film.coverResource
                        TypeScreen.FAVOURITES -> film.slideShowResource
                    }
                    FilmEntityViewMapper(imageResource).apply(film)
                }.let { filmEntityViewList ->
                    Resource.Success(filmEntityViewList)
                }
            }
        }
    }

    fun browse(searchKey: String) {
        if (this.searchKey != searchKey) {
            this.searchKey = searchKey
            executeBrowse()
        }
    }

    private fun executeBrowse(){
        when (typeScreen) {
            TypeScreen.FILMS -> getFilmListUseCase(this.searchKey)
            TypeScreen.FAVOURITES -> getFavouriteFilmListUseCase(this.searchKey)
        }
    }

    /** set or un-set favourite film*/
    fun setFavourite(favourite: Boolean, position: Int) {
        _filmList[position].let { film ->
            setFavouriteUseCase(SetFavouriteParameters(film.filmId, favourite))
        }
    }

    fun isListEmpty(): Boolean {
        return _filmList.isEmpty()
    }

    fun setInfoAsNoMatchesFound() {
        _infoMessage.value = R.string.message_no_matches
    }

    fun setInfoNetWorkProblem() {
        _infoMessage.value = R.string.message_error_connection_swipe
    }

    fun setInfoNoData() {
        _infoMessage.value = R.string.message_no_film_data
    }

    fun retry() {
        executeBrowse()
    }
}

data class FilmEntityView(
    val id: String,
    val imageSrc: String,
    val title: String,
    val year: Int,
    val durationHours: Int,
    val durationMin: Int,
    val categories: String,
    val favourite: Boolean
)