package com.example.loginusingmvvm.main.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginusingmvvm.main.model.MainModel
import com.example.loginusingmvvm.network.RetrofitClient
import com.example.loginusingmvvm.signin.model.SignInModel
import com.example.loginusingmvvm.util.ID
import com.example.loginusingmvvm.util.Resource
import com.example.loginusingmvvm.util.ResponseCodeCheck
import com.example.loginusingmvvm.util.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutableLiveData: MutableLiveData<Resource<MainModel>> = MutableLiveData()
    var liveData: LiveData<Resource<MainModel>> = mutableLiveData


    fun getProfileData() {
        val id = SharedPref(context).getPrefString(ID,"")
        Log.e("ID", "onResponse: $id")

        mutableLiveData.postValue(Resource.loading(null))

        val retrofit = RetrofitClient.getRetrofit()
        retrofit!!.getProfile(id!!).enqueue(object : Callback<MainModel?> {
            override fun onResponse(call: Call<MainModel?>?, response: Response<MainModel?>?) {
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

            override fun onFailure(call: Call<MainModel?>?, t: Throwable?) {
                Log.e("Failed", "onFailure: $t")
                mutableLiveData.postValue(Resource.error(t!!.message!!, null))
            }
        })
    }

}