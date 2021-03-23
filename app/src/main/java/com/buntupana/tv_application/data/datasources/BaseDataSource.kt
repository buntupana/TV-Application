package com.buntupana.tv_application.data.datasources

import com.buntupana.tv_application.R
import com.buntupana.tv_application.data.ResourcesProvider
import com.buntupana.tv_application.domain.entities.Resource
import com.buntupana.tv_application.domain.exceptions.NetworkException
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource constructor(resourcesProvider: ResourcesProvider) {

    private val networkErrorMessage = resourcesProvider.getString(R.string.message_error_connection)

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.Success(body)
            }
            return Resource.Error(NetworkException(networkErrorMessage))
        } catch (e: Exception) {
            Timber.d(e)
            return Resource.Error(NetworkException(networkErrorMessage))
        }
    }
}

