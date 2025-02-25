package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class Seat(
    val id: Long,
    val seatNumber: Int,
    var available: Boolean,
    var user: User? = null
)