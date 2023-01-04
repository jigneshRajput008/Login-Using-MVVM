package com.example.loginusingmvvm.signin.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.network.RetrofitClient
import com.example.loginusingmvvm.signin.model.SignInModel
import com.example.loginusingmvvm.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val util = Utils()

    var email = ""
    var password = ""

    var emailError = ""
    var passwordError = ""

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutableLiveData: MutableLiveData<Resource<SignInModel>> = MutableLiveData()
    var liveData: LiveData<Resource<SignInModel>> = mutableLiveData


    fun validation(): Boolean {

        emailError = ""
        passwordError = ""

        when {
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
            else -> {
                return true
            }
        }
    }

    fun getDataInRetrofit() {
        val hasMap: HashMap<String, String> = HashMap()
        hasMap.apply {
            put(EMAIL, email)
            put(PASSWORD, password)
        }

        mutableLiveData.postValue(Resource.loading(null))
        val retrofit = RetrofitClient.getRetrofit()
        retrofit!!.getReq(hasMap).enqueue(object : Callback<SignInModel?> {
            override fun onResponse(call: Call<SignInModel?>?, response: Response<SignInModel?>?) {
                try {
                    val responseBody = response!!.body()!!
                    mutableLiveData.postValue(
                        responseCodeCheck.getResponseResult(
                            Response.success(
                                responseBody
                            )
                        )
                    )
                }catch (e:Exception){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SignInModel?>?, t: Throwable?) {
                Log.e("Failed", "onFailure: $t")
                mutableLiveData.postValue(Resource.error(t!!.message!!, null))
            }
        })
    }

}