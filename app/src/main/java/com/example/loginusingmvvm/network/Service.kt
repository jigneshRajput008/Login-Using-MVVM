package com.example.loginusingmvvm.network

import com.example.loginusingmvvm.main.model.MainModel
import com.example.loginusingmvvm.signin.model.SignInModel
import com.example.loginusingmvvm.signup.model.SignUpModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url

interface Service {
    @Multipart
    @POST("register.php")
   suspend fun sendData(
        @Part profile_picture:MultipartBody.Part,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<SignUpModel>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun getReq(
        @FieldMap hashMap: HashMap<String, String>
    ): Response<SignInModel>

    @FormUrlEncoded
    @POST("profile.php")
    suspend fun getProfile(
//        @Field("user_id") user_id: String
        @FieldMap hashMap: HashMap<String, String>
    ): Response<MainModel>

}