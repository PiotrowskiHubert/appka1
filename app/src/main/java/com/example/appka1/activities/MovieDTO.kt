package com.example.appka1.activities

import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    val id: Long,
    val title: String,
    val description: String
)