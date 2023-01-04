package com.example.loginusingmvvm.util

import retrofit2.Response

open class ResponseCodeCheck {

    open fun <T> getResponseResult(response: Response<T>): Resource<T> {
        return if (response.code() == 200) {
            Resource.success(response.body())
        } else {
            Resource.error("", response.body())
        }
    }

}