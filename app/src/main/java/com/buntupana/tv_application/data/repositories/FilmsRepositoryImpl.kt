package com.buntupana.tv_application.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.buntupana.tv_application.data.dao.CategoryDao
import com.buntupana.tv_application.data.dao.FavouriteDao
import com.buntupana.tv_application.data.dao.FilmDao
import com.buntupana.tv_application.data.dao.RecommendationDao
import com.buntupana.tv_application.data.datasources.FilmRemoteDataSource
import com.buntupana.tv_application.data.entities.FavouriteEntity
import com.buntupana.tv_application.data.entities.FilmRecommendationCrossRef
import com.buntupana.tv_application.data.resultListLiveData
import com.buntupana.tv_application.data.resultLiveData
import com.buntupana.tv_application.data.utils.*
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Recommendation
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.providers.UrlProvider
import com.buntupana.tv_application.domain.repositories.FilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val filmRemoteDataSource: FilmRemoteDataSource,
    private val filmDao: FilmDao,
    private val favouriteDao: FavouriteDao,
    private val recommendationDao: RecommendationDao,
    private val categoryDao: CategoryDao,
    private val urlProvider: UrlProvider
) : FilmRepository {

    override fun getFilmList(): LiveData<Resource<List<Film>>> {
        return resultListLiveData(
            databaseQuery = {
                filmDao.getFilmList().map { filmAndFavouriteAndCategoriesList ->
                    filmAndFavouriteAndCategoriesList.map { filmAndFavouriteAndCategories ->
                        FilmModelMapper(
                            filmAndFavouriteAndCategories.favourite,
                            filmAndFavouriteAndCategories.categoryList,
                            urlProvider.getImageSourceBaseUrl()
                        ).apply(filmAndFavouriteAndCategories.film)
                    }
                }
            },
            networkCall = { filmRemoteDataSource.fetchFilmList() },
            saveCallResult = { filmsResponse ->
                filmsResponse.response.map { FilmEntityMapper().apply(it) }.let { filmEntityList ->
                    filmDao.insertFilmList(filmEntityList)
                    // inserting list of favourites
                    filmEntityList.map { filmEntity -> FavouriteEntity(filmEntity.filmId) }
                        .let { favouriteEntityList ->
                            favouriteDao.insertFavouriteList(favouriteEntityList)
                        }
                }
                // Creating a full list of CategoryList
                filmsResponse.response.flatMap { filmRaw -> filmRaw.genreEntityList }
                    .let { genreEntityList ->
                        genreEntityList.map { CategoryEntityMapper().apply(it) }
                            .let { categoryEntityList ->
                                categoryDao.insertCategoryList(categoryEntityList)
                            }
                    }
            }
        )
    }

    override fun getFilmsFavourites(): LiveData<List<Film>> {

        return favouriteDao.getFavouriteList().map { favouriteAndFilmAndCategoriesList ->
            favouriteAndFilmAndCategoriesList.map { favouriteAndFilm ->
                FilmModelMapper(
                    favouriteAndFilm.favourite,
                    favouriteAndFilm.categoryList,
                    urlProvider.getImageSourceBaseUrl()
                ).apply(favouriteAndFilm.film)
            }
        }
    }

    override fun setFavourite(filmId: String, favourite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteDao.setFavourite(filmId, favourite)
        }
    }

    override fun getRecommendationList(filmId: String): LiveData<Resource<List<Recommendation>>> {
        return resultListLiveData(
            databaseQuery = {
                filmDao.getFilmWithRecommendations(filmId).map { filmWithRecommendations ->
                    filmWithRecommendations.recommendationList.map { recommendationEntity ->
                        RecommendationModelMapper().apply(
                            recommendationEntity
                        )
                    }
                }
            },
            networkCall = { filmRemoteDataSource.fetchRecommendationList(filmId) },
            saveCallResult = { recommendationResponse ->
                recommendationResponse.response.map { RecommendationEntityMapper().apply(it) }
                    .let { recommendationEntityList ->
                        // update cross reference between films and recommendations
                        recommendationEntityList.map { recommendationEntity ->
                            FilmRecommendationCrossRef(
                                filmId,
                                recommendationEntity.recommendationId
                            )
                        }.let { crossRefList ->
                            filmDao.insertFilmRecommendationCrossRef(crossRefList)
                        }
                        // inserting recommendations
                        recommendationDao.insertRecommendationList(recommendationEntityList)
                    }
            }
        )
    }

    override fun getFavouritesCount(): LiveData<Int> {
        return favouriteDao.getFavouriteCount()
    }

    override fun getFilm(filmId: String): LiveData<Resource<Film>> {
        return resultLiveData(
            databaseQuery = {
                filmDao.getFilm(filmId).map { filmAndFavouriteAndCategories ->
                    FilmModelMapper(
                        filmAndFavouriteAndCategories.favourite,
                        filmAndFavouriteAndCategories.categoryList,
                        urlProvider.getImageSourceBaseUrl()
                    ).apply(
                        filmAndFavouriteAndCategories.film
                    )
                }
            },
            networkCall = { filmRemoteDataSource.fetchFilm(filmId) },
            saveCallResult = { filmResponse ->
                FilmEntityMapper().apply(filmResponse.response).let { filmEntity ->
                    filmDao.insertFilm(filmEntity)
                }
            }
        )
    }
}