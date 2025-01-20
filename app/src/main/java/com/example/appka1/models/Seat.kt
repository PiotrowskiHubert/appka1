package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class Seat(
    val id: Long,
    val seatNumber: Int,
    val available: Boolean
)