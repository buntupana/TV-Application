package com.buntupana.tv_application.domain.usecases

import androidx.lifecycle.MediatorLiveData
import com.buntupana.tv_application.core.MediatorLiveEvent
import com.buntupana.tv_application.domain.entities.Resource

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Resource].
 * Handling an exception (emit [Resource.Error] to the result) is the subclasses's responsibility.
 */
abstract class MediatorUseCase<in P, R> {

    protected val result = MediatorLiveData<Resource<R>>()

    // Make this as open so that mock instances can mock this method
    open fun observe(): MediatorLiveData<Resource<R>> {
        return result
    }

    operator fun invoke(params: P) = execute(params)


    abstract fun execute(parameters: P)
}

abstract class MediatorEventUseCase<in P, R> {

    protected val result = MediatorLiveEvent<Resource<R>>()

    // Make this as open so that mock instances can mock this method
    open fun observe(): MediatorLiveEvent<Resource<R>> {
        return result
    }

    abstract fun execute(parameters: P)
}