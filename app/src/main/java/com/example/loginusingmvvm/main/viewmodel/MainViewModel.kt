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
import com.example.loginusingmvvm.main.repository.MainRepository
import com.example.loginusingmvvm.network.RetrofitClient
import com.example.loginusingmvvm.network.Service
import com.example.loginusingmvvm.signin.model.SignInModel
import com.example.loginusingmvvm.util.ID
import com.example.loginusingmvvm.util.Resource
import com.example.loginusingmvvm.util.ResponseCodeCheck
import com.example.loginusingmvvm.util.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var TAG = ""


    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutableLiveData: MutableLiveData<Resource<MainModel>> = MutableLiveData()
    var liveData: LiveData<Resource<MainModel>> = mutableLiveData


    fun getProfileData() {
        val id = SharedPref(context).getPrefString(ID, "")
        Log.e("ID", "onResponse: $id")

        val hasMap: HashMap<String, String> = HashMap()
        hasMap.apply {
            put("user_id", id!!)
        }

        mutableLiveData.postValue(Resource.loading(null))
        mutableLiveData.value = Resource.loading(null)

        val apiInterface = RetrofitClient.getRetrofit().create(Service::class.java)
        val mainRepository = MainRepository(apiInterface)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository: Response<MainModel> = mainRepository.userProfile(hasMap)
                mutableLiveData.postValue(responseCodeCheck.getResponseResult(repository))
            } catch (e: Exception) {
                Log.d(TAG, "getProfileData: ${e.message}")
                mutableLiveData.postValue(Resource.error(e.message!!, null))
            }
        }

        /*  mutableLiveData.postValue(Resource.loading(null))

        val retrofit = RetrofitClient.getRetrofit()
         retrofit!!.getProfile(hasMap).enqueue(object : Callback<MainModel?> {
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
         })*/
    }

}