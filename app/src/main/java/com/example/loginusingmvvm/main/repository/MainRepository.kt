package com.example.loginusingmvvm.main.repository

import com.example.loginusingmvvm.main.model.MainModel
import com.example.loginusingmvvm.network.Service
import retrofit2.Response

class MainRepository constructor(private val service: Service) {
    suspend fun userProfile(hashMap:HashMap<String,String>): Response<MainModel> {
        return service.getProfile(hashMap)
    }
}