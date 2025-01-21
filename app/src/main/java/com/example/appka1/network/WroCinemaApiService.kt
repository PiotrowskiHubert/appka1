package com.example.appka1.network

import com.example.appka1.models.Seat
import com.example.appka1.models.Showing
import com.example.appka1.models.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query


val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private const val BASE_URL =
    "http://10.0.2.2:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client) // Dodajemy klienta z interceptorami
    .build()

interface WroCinemaApiService {
    @GET("/login")
    suspend fun userLogin(@Query("email") email: String, @Query("password") password: String): User

    @GET("/showings")
    suspend fun getShowings(): List<Showing>

    @GET("/get-showing-by-id")
    suspend fun getShowing(@Query("id") id: Long): Showing

    @PATCH("/update-showing-by-id")
    @JvmSuppressWildcards
    suspend fun updateShowing(@Query("id") id: Long, @Body reservedSeats: Map<String, List<Seat>>): Showing
}

object WroCinemaApi {
    val retrofitService : WroCinemaApiService by lazy {
        retrofit.create(WroCinemaApiService::class.java)
    }
}