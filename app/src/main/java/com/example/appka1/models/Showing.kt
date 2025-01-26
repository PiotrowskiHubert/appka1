package com.example.appka1.models

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Showing(
    val id: Long,
    val movie: Movie,
    val screeningRoom: ScreeningRoom,
    val startTime: String,
    val seats: MutableList<Seat>,
    val date: LocalDate
) {
    override fun toString(): String {
        return "${movie.title} - ${startTime} - ${screeningRoom.name}"
    }
}