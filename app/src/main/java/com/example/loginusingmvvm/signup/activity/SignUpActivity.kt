package com.example.loginusingmvvm.signup.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loginusingmvvm.R
import com.example.loginusingmvvm.signin.activity.SignInActivity
import com.example.loginusingmvvm.signup.viewmodel.SignUpViewModel
import com.example.loginusingmvvm.util.PathUtil
import com.example.loginusingmvvm.util.Status
import com.example.loginusingmvvm.util.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {
    lateinit var binding: com.example.loginusingmvvm.databinding.ActivitySignUpBinding
    lateinit var signUpViewModel: SignUpViewModel
    lateinit var util: Utils
    private lateinit var progressDialog: ProgressDialog

    var path: String = ""

    companion object {
        const val REQUEST_GALLERY = 1
        const val REQUEST_CAMERA = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.signUp = signUpViewModel
        binding.lifecycleOwner = this

        supportActionBar?.hide()
        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        util = Utils()
        progressDialog = ProgressDialog(this)


        onClick(signUpViewModel)

        signUpViewModel.liveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    val message = it.data!!.message

                    if (it.data.status) {
                        util.dialogBox(this, getString(R.string.dialog_title_Sign_up), message, 2)
                        clearEditText()
                    } else {
                        util.dialogBox(this, getString(R.string.dialog_title_Sign_up), message, 3)
                    }
                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.progress_dialog_loading))
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    Log.e("SIGN_IN", "Status.LOADING")
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e("SIGN_IN", "Status.ERROR")
                }
            }
        }
    }

    private fun onClick(signUpViewModel: SignUpViewModel) {
        binding.apply {
            btnSignUp.setOnClickListener {
                if (util.isNetworkAvailable(this@SignUpActivity)) {
                    signUpViewModel.apply {

                        edtFullName.error = null
                        edtEmail.error = null
                        edtPassword.error = null

                        imagePath = path

                        if (validation()) {
                            sendDataInRetrofit()
                            util.keyBoardHide(this@SignUpActivity)
                        } else {
                            edtFullName.error = fullNameError
                            edtEmail.error = emailError
                            edtPassword.error = passwordError
                        }
                    }
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.toast_title_No_Internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btnSelectImage.setOnClickListener {
                dialogBoxTwo(this@SignUpActivity, getString(R.string.dialog_title_select_image))
            }
            txtSignIn.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }
        }
    }

    private fun clearEditText() {
        binding.apply {
            edtFullName.editText!!.text.clear()
            edtEmail.editText!!.text.clear()
            edtPassword.editText!!.text.clear()
        }
    }

    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@SignUpActivity, permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this@SignUpActivity, arrayOf(permission), requestCode)
        } else {
            if (requestCode == REQUEST_CAMERA) {
                openCamera()
            } else if (requestCode == REQUEST_GALLERY) {
                openGallery()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            path = if (requestCode == REQUEST_CAMERA) {
                val pic = data?.getParcelableExtra<Bitmap>("data")
                binding.imgLogo.setImageBitmap(pic)

                val uri = getImageUri(pic!!)
                PathUtil.getPath(this, uri!!).toString()

            } else {
                val uri = data?.data
                binding.imgLogo.setImageURI(uri)
                PathUtil.getPath(this, uri!!).toString()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.toast_title_gallery_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
                openGallery()
            } else {
                val shouldShowRequest =
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                if (!shouldShowRequest) {
                    util.openSettingDialog(this,getString(R.string.alertdialog_gallery_permission))
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.toast_title_gallery_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.toast_title_camera_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
                openCamera()
            } else {
                val shouldShowRequest =
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                if (!shouldShowRequest) {
                    util.openSettingDialog(this,getString(R.string.alertdialog_camera_permission))
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.toast_title_camera_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityIfNeeded(intent, REQUEST_GALLERY)

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityIfNeeded(intent, REQUEST_CAMERA)
    }

    fun dialogBoxTwo(activity: Activity, title: String) {
        activity.apply {
            MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setPositiveButton("Gallery") { gallery, _ ->
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_GALLERY)
                }
                .setNegativeButton("Camera") { camera, _ ->
                    checkPermission(Manifest.permission.CAMERA, REQUEST_CAMERA)

                }
                .setNeutralButton("Dismiss") { dismiss, _ ->
                    dismiss.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val cameraPath = MediaStore.Images.Media.insertImage(this.contentResolver, inImage, "Title", null)
        return Uri.parse(cameraPath)
    }

}