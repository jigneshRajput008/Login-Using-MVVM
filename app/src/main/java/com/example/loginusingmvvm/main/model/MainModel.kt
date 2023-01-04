package com.example.loginusingmvvm.main.model


class MainModel(
    val status: Boolean,
    val message: String,
    val data: GetProfileData
) {
    inner class GetProfileData(
        val id: String,
        val name: String,
        val email: String,
        val profile_picture: String
    )
}
