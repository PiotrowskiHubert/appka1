package com.example.appka1.network

import android.icu.text.CaseMap.Title
import com.example.appka1.models.Movie
import com.example.appka1.models.ReservedSeats
import com.example.appka1.models.Seat
import com.example.appka1.models.Showing
import com.example.appka1.models.User
import com.example.appka1.RegisterUserDTO
import com.example.appka1.activities.MovieDTO
import com.example.appka1.models.ScreeningRoom
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate


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

    @GET("/movies")
    suspend fun getAllMovies(): List<MovieDTO>

    @GET("/showings")
    suspend fun getShowings(): List<Showing>

    @GET("/get-showing-by-id")
    suspend fun getShowing(@Query("id") id: Long): Showing

    @PATCH("/update-showing-by-id")
    @JvmSuppressWildcards
    suspend fun updateShowing(@Query("id") id: Long, @Body reservedSeats: Map<String, List<Seat>>, @Query("userId") userId: Long): Showing

    @GET("/seats-reserved-by-user")
    suspend fun getSeatsReservedByUser(@Query("userId") userId: Long): List<ReservedSeats>

    @GET("/get-showings-by-date")
    suspend fun getShowingsByDate(@Query("day") day: Int, @Query("month") month: Int, @Query("year") year: Int): List<Showing>

    @GET("/find-movie-by-title")
    suspend fun findMovieByTitle(@Query("title") title: String): Movie

    @GET("/{id}/poster")
    suspend fun getMoviePoster(@Path("id") id: Long): Call<ResponseBody>

    @POST("/register")
    suspend fun registerUser(@Body registerUser: RegisterUserDTO): User

    @POST("/add-movie")
    suspend fun addMovie(@Query("title") title: String, @Query("description") description: String)

    @POST("/TODO")
    @Multipart
    suspend fun addMovieWithPoster(@Part("poster") poster: MultipartBody.Part): String

    @DELETE("/delete-movie")
    suspend fun deleteMovie(@Query("id") id: Long): Void

    @POST("/add-screening-room")
    suspend fun addScreeningRoom(@Query("name") name: String, @Query("numOfSeats") numOfSeats: Int): ScreeningRoom

    @GET("/get-all-screening-rooms")
    suspend fun getAllScreeningRooms(): List<ScreeningRoom>

    @DELETE("/delete-screening-room-by-name")
    suspend fun deleteScreeningRoom(@Query("name") name: String): Void

    @POST("/add-showing")
    suspend fun addShowing(
        @Query("movieTitle") movieTitle: String,
        @Query("screeningRoomName") screeningRoomName: String,
        @Query("startTime") startTime: String,
        @Query("date") date: LocalDate
    ): Showing

    @DELETE("/TODO")
    suspend fun deleteShowing(): Showing
}

object WroCinemaApi {
    val retrofitService : WroCinemaApiService by lazy {
        retrofit.create(WroCinemaApiService::class.java)
    }
}