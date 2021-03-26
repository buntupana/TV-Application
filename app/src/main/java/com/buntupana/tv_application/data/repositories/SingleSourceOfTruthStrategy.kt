package com.buntupana.tv_application.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.buntupana.tv_application.domain.entities.Resource
import kotlinx.coroutines.Dispatchers

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Resource.Success] - with data from database
 * [Resource.Error] - if error has occurred from any source
 * [Resource.Loading]
 */
fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        var networkLoading = true
        var data: T? = null

        val source = databaseQuery.invoke().map {
            if (it == null) {
                Resource.Loading()
            } else {
                data = it
                Resource.Success<T>(it, networkLoading)
            }
        }

        emitSource(source)

        val responseStatus = networkCall.invoke()
        networkLoading = false

        if (responseStatus is Resource.Success) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus is Resource.Error) {
            emit(Resource.Error(responseStatus.exception, data))
        }
    }

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Resource.Success] - with data from database
 * [Resource.Error] - if error has occurred from any source
 * [Resource.Loading]
 */
fun <T, A> resultListLiveData(
    databaseQuery: () -> LiveData<List<T>>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Int
): LiveData<Resource<List<T>>> =
    liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        var networkLoading = true
        var data: List<T>? = null

        val source = databaseQuery.invoke().map {
            if (it.isEmpty() && networkLoading) {
                Resource.Loading()
            } else {
                data = it
                Resource.Success(it, networkLoading)
            }
        }

        emitSource(source)

        val responseStatus = networkCall.invoke()
        networkLoading = false

        if (responseStatus is Resource.Success) {
            if (saveCallResult(responseStatus.data!!) == 0) {
                emit(Resource.Success(listOf()))
            }
        } else if (responseStatus is Resource.Error) {
            emit(Resource.Error(responseStatus.exception, data))
            emitSource(source)
        }
    }
