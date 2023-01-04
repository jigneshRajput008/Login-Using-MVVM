package com.example.loginusingmvvm.signup.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.network.RetrofitClient
import com.example.loginusingmvvm.signup.model.SignUpModel
import com.example.loginusingmvvm.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val util = Utils()

    var fullName = ""
    var email = ""
    var password = ""
    var imagePath = ""

    var fullNameError = ""
    var emailError = ""
    var passwordError = ""

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutableLiveData: MutableLiveData<Resource<SignUpModel>> = MutableLiveData()
    var liveData: LiveData<Resource<SignUpModel>> = mutableLiveData

    fun validation(): Boolean {

        fullNameError = ""
        emailError = ""
        passwordError = ""

        when {
            util.fullNameIsEmpty(fullName) -> {
                fullNameError =
                    getApplication<Application>().getString(R.string.full_name_error_name_is_required)
                return false
            }
            util.emailIsEmpty(email) -> {
                emailError =
                    getApplication<Application>().getString(R.string.email_error_name_is_required)
                return false
            }
            util.isValidEmail(email) -> {
                emailError =
                    getApplication<Application>().getString(R.string.email_error_email_is_not_valid)
                return false
            }
            util.passwordIsEmpty(password) -> {
                passwordError =
                    getApplication<Application>().getString(R.string.password_error_name_is_required)
                return false
            }
            util.isValidPassword(password) -> {
                passwordError =
                    getApplication<Application>().getString(R.string.password_error_atLeast_six_character)
                return false
            }
             imagePath.isEmpty()->{
                 Toast.makeText(context, context.getString(R.string.toast_please_select_image), Toast.LENGTH_SHORT).show()
                 return false
             }
            else -> {
                return true
            }
        }
    }

    fun sendDataInRetrofit() {

        val file = File(imagePath)

        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)

        val sendName: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fullName)
        val sendEmail: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        val sendPassword: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)

        Log.e("TAG", "sendDataInRetrofit: $body" )
        Log.e("TAG", "sendDataInRetrofit: $imagePath" )

        val hasMap: HashMap<String, RequestBody> = HashMap()
        hasMap.apply {
            put("name", sendName)
            put("email", sendEmail)
            put("password", sendPassword)
        }

        mutableLiveData.postValue(Resource.loading(null))
        val retrofit = RetrofitClient.getRetrofit()
        retrofit!!.sendData(body, hasMap).enqueue(object : Callback<SignUpModel?> {
            override fun onResponse(call: Call<SignUpModel?>?, response: Response<SignUpModel?>?) {
                try {
                    val responseBody = response!!.body()!!
                    mutableLiveData.postValue(
                        responseCodeCheck.getResponseResult(
                            Response.success(
                                responseBody
                            )
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpModel?>?, t: Throwable?) {
                Log.e("Failed", "onFailure: $t")
                mutableLiveData.postValue(Resource.error(t!!.message!!, null))
            }
        })
    }


}