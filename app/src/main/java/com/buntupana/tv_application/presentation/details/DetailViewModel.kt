package com.buntupana.tv_application.presentation.details

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Recommendation
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.usecases.GetFilmUseCase
import com.buntupana.tv_application.domain.usecases.GetRecommendationListUseCase
import com.buntupana.tv_application.domain.usecases.SetFavouriteParameters
import com.buntupana.tv_application.domain.usecases.SetFavouriteUseCase
import com.buntupana.tv_application.presentation.common.DefaultItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class DetailViewModel @AssistedInject constructor(
    private val getFilmUseCase: GetFilmUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase,
    private val getRecommendationListUseCase: GetRecommendationListUseCase,
    @Assisted private val filmId: String
) : ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(filmId: String): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            filmId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(filmId) as T
            }
        }
    }

    /** for the fragment to listen */
    val filmResourceState: LiveData<Resource<Unit>>

    /** to populate film view */
    private val _filmViewEntity = MutableLiveData<FilmViewEntity>()
    val filmViewEntity: LiveData<FilmViewEntity>
        get() = _filmViewEntity

    val recommendationResource: LiveData<Resource<List<RecommendationViewEntity>>>
    private var recommendationList = listOf<Recommendation>()

    init {
        // observing film results
        filmResourceState = getFilmUseCase.observe().map { resource ->
            when (resource) {
                is Resource.Error -> {
                    Resource.Error(resource.exception)
                }
                is Resource.Loading -> Resource.Loading()
                is Resource.Success -> {
                    if (resource.data == null) {
                        Resource.Error(resource.exception)
                    } else {
                        _filmViewEntity.value = FilmViewEntityMapper().apply(resource.data)
                        Resource.Success(Unit)
                    }
                }
            }
        }

        // observing recommendations results
        recommendationResource = getRecommendationListUseCase.observe().map { resource ->
            when (resource) {
                is Resource.Error -> Resource.Error(resource.exception)
                is Resource.Loading -> Resource.Loading()
                is Resource.Success -> {
                    recommendationList = resource.data ?: listOf()
                    val result = resource.data!!.map { recommendation ->
                        RecommendationEntityViewMapper().apply(recommendation)
                    }
                    Resource.Success(result)
                }
            }
        }

        getFilmUseCase(filmId)
        getRecommendationListUseCase(filmId)
    }

    fun retry() {
        getFilmUseCase(filmId)
        getRecommendationListUseCase(filmId)
    }

    fun getRecommendationId(position: Int): String {
        return recommendationList[position].recommendationId
    }

    fun changeFavourite() {
        filmViewEntity.value?.let {
            setFavouriteUseCase(SetFavouriteParameters(filmId, !it.favourite))
        }
    }

    data class FilmViewEntity(
        val filmId: String,
        val title: String,
        val coverResource: String,
        val slideShowResource: String,
        val favourite: Boolean,
        val year: Int,
        val durationHours: Int,
        val durationMin: Int,
        val categories: String,
        val plot: String
    )

    data class RecommendationViewEntity(
        val filmId: String,
        val title: String,
        val coverResource: String
    ) : DefaultItem(filmId.hashCode().toLong())

    class FilmViewEntityMapper : Function<Film, FilmViewEntity> {
        override fun apply(input: Film): FilmViewEntity {
            var durationMin = (input.duration / 1000 / 60).toInt()
            val durationHours = durationMin / 60
            durationMin = durationMin.rem(60)
            val categories =
                input.categoryList.joinToString(separator = ", ") { category -> category.name }
            return FilmViewEntity(
                input.filmId,
                input.title,
                input.coverResource,
                input.slideShowResource,
                input.favourite,
                input.year,
                durationHours,
                durationMin,
                categories,
                input.plot
            )
        }
    }

    class RecommendationEntityViewMapper : Function<Recommendation, RecommendationViewEntity> {
        override fun apply(input: Recommendation): RecommendationViewEntity {
            return RecommendationViewEntity(
                input.recommendationId,
                input.title,
                input.imageSource
            )
        }
    }
}