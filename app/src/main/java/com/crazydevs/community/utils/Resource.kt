package com.crazydevs.community.utils

sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data)
    class Failure<T>(message: String?): Resource<T>(error = message)
}