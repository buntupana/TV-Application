package com.buntupana.tv_application.domain.entities

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val exception: Exception = Exception(),
    val fromNetwork: Boolean = false
) {
    class Success<T>(data: T, fromNetwork: Boolean = false) :
        Resource<T>(data, fromNetwork = fromNetwork)

    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception, data: T? = null) :
        Resource<T>(data, exception.message, exception)
}