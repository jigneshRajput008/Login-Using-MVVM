package com.example.loginusingmvvm.splash.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.RenderProcessGoneDetail
import androidx.databinding.DataBindingUtil
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.databinding.ActivitySplashScreenBinding
import com.example.loginusingmvvm.main.activity.MainActivity
import com.example.loginusingmvvm.signin.activity.SignInActivity
import com.example.loginusingmvvm.signup.activity.SignUpActivity
import com.example.loginusingmvvm.util.IS_LOGIN
import com.example.loginusingmvvm.util.SharedPref

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val milliSeconds: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        supportActionBar?.hide()
        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        handler()

    }

    private fun handler() {
        binding.apply {
            val isLogin = SharedPref(this@SplashScreenActivity).getPrefBoolean(IS_LOGIN)
            if (isLogin) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }, milliSeconds)
                animation.visibility = View.VISIBLE
                txtStuff.visibility = View.GONE
                btnGettingStarted.visibility = View.GONE
                txtAlreadyHaveAnAccount.visibility = View.GONE
                txtSignIn.visibility = View.GONE
            } else {
                onClick()
                animation.visibility = View.GONE
                txtStuff.visibility = View.VISIBLE
                btnGettingStarted.visibility = View.VISIBLE
                txtAlreadyHaveAnAccount.visibility = View.VISIBLE
                txtSignIn.visibility = View.VISIBLE
            }
        }
    }

    private fun onClick() {
        binding.apply {
            btnGettingStarted.setOnClickListener {
                startActivity(Intent(this@SplashScreenActivity, SignUpActivity::class.java))
                finish()
            }
            txtSignIn.setOnClickListener {
                startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
                finish()
            }
        }
    }
}