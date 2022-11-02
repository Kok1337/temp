package com.kok1337.result

typealias Mapper<Input, Output> = (Input) -> Output

sealed class DataResult<T> {
    fun <R> map(mapper: Mapper<T, R>? = null): DataResult<R> = when (this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Null mapper for SuccessResult")
            SuccessResult(mapper(this.data))
        }
    }
}

class PendingResult<T> : DataResult<T>()

class SuccessResult<T>(
    val data: T
) : DataResult<T>()

class ErrorResult<T>(
    val exception: Exception
) : DataResult<T>()

fun <T> DataResult<T>?.takeSuccess(): T? {
    return if (this is SuccessResult) this.data else null
}