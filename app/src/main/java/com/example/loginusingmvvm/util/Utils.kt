package com.example.loginusingmvvm.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import com.airbnb.lottie.BuildConfig
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.main.activity.MainActivity
import com.example.loginusingmvvm.signin.activity.SignInActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Utils {
    fun fullNameIsEmpty(fullName: String): Boolean {
        return fullName.isEmpty()
    }

    fun passwordIsEmpty(password: String): Boolean {
        return password.isEmpty()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length < 6
    }

    fun emailIsEmpty(email: String): Boolean {
        return email.isEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun keyBoardHide(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    fun dialogBox(activity: Activity, title: String, message: String, isCheck: Int) {
        activity.apply {
            val dialog = MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok") { _, _ ->
                    when (isCheck) {
                        0 -> {
                            SharedPref(activity).setPrefBoolean(IS_LOGIN, true)
                            startActivity(Intent(activity, MainActivity::class.java))
                            finish()
                        }
                        2 -> {
                            startActivity(Intent(activity, SignInActivity::class.java))
                            finish()
                        }
                    }
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    fun openSettingDialog(activity: Activity, message: String) {
        val openSettingDialogBox = MaterialAlertDialogBuilder(activity, R.style.alertDialogStyle)
        openSettingDialogBox.setMessage(message)
        openSettingDialogBox.setPositiveButton("Settings") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package",BuildConfig.APPLICATION_ID , null)
            activity.startActivity(intent)
        }
        openSettingDialogBox.setNegativeButton("Dismiss") { deny, _ ->
            deny.dismiss()
        }
        openSettingDialogBox.setCancelable(false)
        openSettingDialogBox.create()
        openSettingDialogBox.show()
    }
}
