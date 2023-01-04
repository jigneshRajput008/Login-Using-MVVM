package com.example.loginusingmvvm.main.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.databinding.ActivityMainBinding
import com.example.loginusingmvvm.main.viewmodel.MainViewModel
import com.example.loginusingmvvm.signin.activity.SignInActivity
import com.example.loginusingmvvm.util.IS_LOGIN
import com.example.loginusingmvvm.util.SharedPref
import com.example.loginusingmvvm.util.Status

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.main = mainViewModel
        binding.lifecycleOwner = this

        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        mainViewModel.apply {
            getProfileData()
        }

        mainViewModel.liveData.observe(this){
            when(it.status){
                Status.SUCCESS->{
                    if(it.data!!.status){
                        Log.d("data_information", it.data.data.profile_picture)
                        Glide.with(this)
                            .load(it.data.data.profile_picture)
                            .placeholder(R.drawable.ic_google_plus)
                            .into(binding.imgProfilePicture)
                        binding.txtName.text = it.data.data.name
                        binding.txtEmail.text = it.data.data.email

                    }else{
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{

                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optLogOut->{
                SharedPref(this).setPrefBoolean(IS_LOGIN,false)
                startActivity(Intent(this,SignInActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}