package com.example.loginusingmvvm.signup.repository

import com.example.loginusingmvvm.network.Service
import com.example.loginusingmvvm.signup.model.SignUpModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SignUpRepository constructor(private val service: Service){
    suspend fun senData(profile_picture: MultipartBody.Part, hashMap:HashMap<String,RequestBody>):Response<SignUpModel>{
        return service.sendData(profile_picture,hashMap)
    }
}