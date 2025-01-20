package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class ScreeningRoom(
    val id: Long,
    val name: String,
    val numOfSeats: Int
)