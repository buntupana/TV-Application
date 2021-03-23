package com.buntupana.tv_application.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.buntupana.tv_application.data.dao.FavouriteDao
import com.buntupana.tv_application.data.dao.FilmDao
import com.buntupana.tv_application.data.dao.RecommendationDao
import com.buntupana.tv_application.data.datasources.FilmRemoteDataSource
import com.buntupana.tv_application.data.entities.FavouriteEntity
import com.buntupana.tv_application.data.entities.FilmRecommendationCrossRef
import com.buntupana.tv_application.data.resultListLiveData
import com.buntupana.tv_application.data.resultLiveData
import com.buntupana.tv_application.data.utils.FilmEntityMapper
import com.buntupana.tv_application.data.utils.FilmModelMapper
import com.buntupana.tv_application.data.utils.RecommendationEntityMapper
import com.buntupana.tv_application.data.utils.RecommendationModelMapper
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Recommendation
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.repositories.FilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val filmRemoteDataSource: FilmRemoteDataSource,
    private val filmDao: FilmDao,
    private val favouriteDao: FavouriteDao,
    private val recommendationDao: RecommendationDao
) : FilmRepository {

    override fun getFilmList(): LiveData<Resource<List<Film>>> {
        return resultListLiveData(
            databaseQuery = {
                filmDao.getFilmList().map { filmAndFavouriteList ->
                    filmAndFavouriteList.map { filmAndFavourite ->
                        FilmModelMapper(
                            filmAndFavourite.favourite?.favourite ?: false
                        ).apply(filmAndFavourite.film)
                    }
                }
            },
            networkCall = { filmRemoteDataSource.fetchFilmList() },
            saveCallResult = { filmsResponse ->
                filmsResponse.response.map { FilmEntityMapper().apply(it) }.let { filmEntityList ->
                    filmDao.insertFilmList(filmEntityList)
                    filmEntityList.map { filmEntity -> FavouriteEntity(filmEntity.filmId) }
                        .let { favouriteEntityList ->
                            favouriteDao.insertFavouriteList(favouriteEntityList)
                        }
                }
            }
        )
    }

    override fun getFilmsFavourites(): LiveData<List<Film>> {

        return favouriteDao.getFavouriteList().map { favouriteAndFilmList ->
            favouriteAndFilmList.map { favouriteAndFilm ->
                FilmModelMapper(favouriteAndFilm.favourite.favourite).apply(favouriteAndFilm.film)
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
                filmDao.getFilm(filmId).map { filmAndFavourite ->
                    FilmModelMapper(filmAndFavourite.favourite?.favourite ?: false).apply(
                        filmAndFavourite.film
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