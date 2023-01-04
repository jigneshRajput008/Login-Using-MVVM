package com.example.loginusingmvvm.signin.model

class SignInModel(
    val status: Boolean,
    val message: String,
    val data: Data
) {
    data class Data(
        val id: String,
        val name: String,
        val email: String
    ) {

    }
}
