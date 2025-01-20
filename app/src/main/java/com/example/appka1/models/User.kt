package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val status: Int
)
