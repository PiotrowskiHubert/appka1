package com.example.appka1.models

import kotlinx.serialization.Serializable

@Serializable
data class Showing(
    val id: Long,
    val movie: Movie,
    val screeningRoom: ScreeningRoom,
    val startTime: String,
    val seats: List<Seat>
) {
    override fun toString(): String {
        return "${movie.title} - ${startTime} - ${screeningRoom.name}"
    }
}