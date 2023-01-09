package com.example.loginusingmvvm.signin.repository

import com.example.loginusingmvvm.network.Service
import com.example.loginusingmvvm.signin.model.SignInModel
import retrofit2.Response

class SignInRepository constructor(private val service: Service) {
    suspend fun getData(hashMap:HashMap<String,String>): Response<SignInModel> {
        return service.getReq(hashMap)
    }
}