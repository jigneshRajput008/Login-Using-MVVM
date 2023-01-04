package com.example.loginusingmvvm.signin.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.databinding.ActivitySignInBinding
import com.example.loginusingmvvm.main.activity.MainActivity
import com.example.loginusingmvvm.main.viewmodel.MainViewModel
import com.example.loginusingmvvm.signin.viewmodel.SignInViewModel
import com.example.loginusingmvvm.signup.activity.SignUpActivity
import com.example.loginusingmvvm.util.ID
import com.example.loginusingmvvm.util.SharedPref
import com.example.loginusingmvvm.util.Status
import com.example.loginusingmvvm.util.Utils

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var signInViewModel: SignInViewModel
    lateinit var util: Utils
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        binding.signIn = signInViewModel
        binding.lifecycleOwner = this

        util = Utils()
        progressDialog = ProgressDialog(this)


        supportActionBar?.hide()
        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        onClick(signInViewModel)

        signInViewModel.liveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()

                    val message = it.data!!.message

                    if (it.data.status) {
                        val id = it.data.data.id
                        SharedPref(this).setPrefString(ID, id)
                        util.dialogBox(this, getString(R.string.dialog_title_Sign_in), message, 0)
                        clearEditText()
                    } else {
                        util.dialogBox(this, getString(R.string.dialog_title_Sign_in), message, 1)
                    }
                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.progress_dialog_loading))
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e("SIGN_IN", "Status.ERROR")
                }
            }
        }

    }


    private fun onClick(signInViewModel: SignInViewModel) {
        binding.apply {
            signInViewModel.apply {
                btnSignIn.setOnClickListener {
                    if (util.isNetworkAvailable(this@SignInActivity)){
                        edtEmail.error = null
                        edtPassword.error = null

                        if (validation()) {
                            getDataInRetrofit()
                            util.keyBoardHide(this@SignInActivity)
                        } else {
                            edtEmail.error = emailError
                            edtPassword.error = passwordError
                        }
                    }else{
                        Toast.makeText(this@SignInActivity,  getString(R.string.toast_title_No_Internet), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            txtSignUp.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                finish()
            }
        }
    }

    private fun clearEditText() {
        binding.apply {
            edtEmail.editText!!.text.clear()
            edtPassword.editText!!.text.clear()
        }
    }
}