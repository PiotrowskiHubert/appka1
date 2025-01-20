package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long,
    val title: String
)