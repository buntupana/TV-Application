package com.buntupana.tv_application.domain.usecases

import com.buntupana.tv_application.domain.entities.Recommendation
import com.buntupana.tv_application.domain.repositories.FilmRepository
import javax.inject.Inject

class GetRecommendationListUseCase @Inject constructor(
    private val filmRepository: FilmRepository
) : MediatorUseCase<String, List<Recommendation>>() {

    override fun execute(parameters: String) {

        result.removeSource(source)
        source = filmRepository.getRecommendationList(parameters)
        result.addSource(source) { resource ->
            result.postValue(resource)
        }
    }
}