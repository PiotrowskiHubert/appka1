package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.models.Seat
import com.example.appka1.models.Showing
import com.example.appka1.models.User
import com.example.appka1.network.WroCinemaApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface SeatsResultsUiState {
    data class Success(val apiSeatsResult: Showing) : SeatsResultsUiState
    object Error : SeatsResultsUiState
    object Loading : SeatsResultsUiState
}

class SeatsActivity : AppCompatActivity() {
    private var seatsApiState: SeatsResultsUiState = SeatsResultsUiState.Loading
    private var showingId: Long = -1L
    private var seatGridWidth: Int = 0
    private var seatGridHeight: Int = 0
    private val reservedSeats: MutableList<Seat> = mutableListOf()
    private lateinit var user: User
    private var showing: Showing? = null

    private fun updateGridLayout(seatGrid: GridLayout, rows: Int, columns: Int) {
        seatGrid.columnCount = columns
        seatGrid.rowCount = rows

        seatGridWidth = seatGrid.width
        seatGridHeight = seatGrid.height

        seatGrid.setPadding(0,8,0,8)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showingId = intent.getLongExtra("SHOWING_ID", -1)
        if (showingId == -1L) {
            finish()
        }
        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("SeatsActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("SeatsActivity", "Błąd: brak danych użytkownika")
        }
        setContentView(R.layout.activity_seats)
        val seatGrid: GridLayout = findViewById(R.id.seat_grid)
        val confirmButton: Button = findViewById(R.id.confirm_button)

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                showing = withContext(Dispatchers.IO) {
                    WroCinemaApi.retrofitService.getShowing(id = showingId)
                }

                if (showing != null) {
                    seatsApiState = SeatsResultsUiState.Success(showing!!)
                    val numberOfSeats = showing!!.seats.size
                    val rows = 7
                    val columns = numberOfSeats / rows
                    updateGridLayout(
                        seatGrid = seatGrid,
                        rows = rows,
                        columns = columns
                    )

                    var seatList: List<Seat> = showing!!.seats
                    manageSeatButtons(seatGrid, seatList)
                } else {
                    seatsApiState = SeatsResultsUiState.Error
                }

            }catch (e: Exception) {
                seatsApiState = SeatsResultsUiState.Error
            }
        }

        confirmButton.setOnClickListener {
            if (reservedSeats.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        WroCinemaApi.retrofitService.updateShowing(id = showingId, reservedSeats = mapOf("seats" to reservedSeats), userId = user.id)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SeatsActivity, "Zarezerwowano ${reservedSeats.size} miejsc!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@SeatsActivity, ReservedSeatsActivity::class.java).apply {
                                putExtra("USER", userJson)
                                putExtra("RESERVED_SEATS", ArrayList(reservedSeats.map { it.seatNumber.toString() }))
                                putExtra("SHOWING_TITLE", showing?.movie?.title)
                                putExtra("SHOWING_START_TIME", showing?.startTime)
                            }
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SeatsActivity, "Błąd podczas rezerwacji miejsc!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Wybierz przynajmniej jedno miejsce!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun manageSeatButtons(seatGrid: GridLayout, seatList: List<Seat>) {
        val copySeatList: MutableList<Seat> = seatList.map { it.copy() }.toMutableList()
        copySeatList.forEach { seat ->
            val seatButton = Button(this@SeatsActivity).apply {
                text = seat.seatNumber.toString()

                // Ustawienie koloru w zależności od dostępności
                setBackgroundColor(getColor(if (seat.available) android.R.color.holo_green_light else android.R.color.holo_purple))

                // Jeśli oryginalne miejsce jest niedostępne, użytkownik nie może go zmieniać
                if (!seatList[seat.seatNumber - 1].available) {
                    isEnabled = false // Dezaktywowanie przycisku
                } else {
                    setOnClickListener {
                        // Zmieniamy status tylko jeśli miejsce było dostępne w oryginalnej liście
                        if (seat.available){
                            reservedSeats.add(seat)
                        } else {
                            reservedSeats.remove(seat)
                        }
                        seat.available = !seat.available
                        setBackgroundColor(getColor(if (seat.available) android.R.color.holo_green_light else android.R.color.holo_purple))
                        println("CSeat ${seat.seatNumber} is now ${if (seat.available) "available" else "unavailable"}")
                        reservedSeats.forEach {
                            println("Reserved seat: ${it.seatNumber}")
                        }
                    }
                }
            }

            val params = GridLayout.LayoutParams().apply {
                width = 100
                height = 100
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            seatGrid.addView(seatButton, params)
        }
    }
}