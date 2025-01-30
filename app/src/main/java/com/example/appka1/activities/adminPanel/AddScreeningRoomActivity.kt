package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch

class AddScreeningRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_screening_room)

        val addScreeningRoomButton = findViewById<Button>(R.id.adminAddScreeningRoomButton)

        addScreeningRoomButton.setOnClickListener {
            lifecycleScope.launch {
                val roomName = findViewById<EditText>(R.id.nameEditText).text.toString()
                val numOfSeats = findViewById<EditText>(R.id.numOfSeatsEditText).text.toString().toInt()
                try {
                    WroCinemaApi.retrofitService.addScreeningRoom(
                        name = roomName,
                        numOfSeats = numOfSeats
                    )
                } catch (e: Exception) {
                    Log.e("adminAddScreeningRoom", e.toString())
                }
            }
        }
    }
}