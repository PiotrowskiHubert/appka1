package com.example.appka1.network

import com.example.appka1.models.Showing
import com.example.appka1.models.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "http://10.0.2.2:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface WroCinemaApiService {
    @GET("/login")
    suspend fun userLogin(@Query("email") email: String, @Query("password") password: String): User

    @GET("/showings")
    suspend fun getShowings(): List<Showing>
}

object WroCinemaApi {
    val retrofitService : WroCinemaApiService by lazy {
        retrofit.create(WroCinemaApiService::class.java)
    }
}