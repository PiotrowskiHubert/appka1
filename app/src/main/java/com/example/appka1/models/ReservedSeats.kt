package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class ReservedSeats(
    val showing: Showing,
    val seatNumber: Int,
    val user: User
)
