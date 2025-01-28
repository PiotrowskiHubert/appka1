package com.example.appka1

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDTO(
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val code: String? = null
)