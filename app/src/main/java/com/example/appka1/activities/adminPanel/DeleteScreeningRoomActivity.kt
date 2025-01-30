package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.appka1.R
import com.example.appka1.models.ScreeningRoom
import com.example.appka1.viewModel.DeleteScreeningRoomViewModel

class DeleteScreeningRoomActivity : AppCompatActivity() {

    private lateinit var roomsContainer: LinearLayout
    private lateinit var viewModel: DeleteScreeningRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete_screening_room)

        roomsContainer = findViewById(R.id.roomsContainer)
        viewModel = ViewModelProvider(this)[DeleteScreeningRoomViewModel::class.java]

        viewModel.screeningRooms.observe(this) { rooms ->
            displayScreeningRooms(rooms)
        }

        viewModel.fetchScreeningRooms()
    }

    private fun displayScreeningRooms(rooms: List<ScreeningRoom>) {
        roomsContainer.removeAllViews()

        if (rooms.isEmpty()) {
            println("No screening rooms found. UI should be empty.")
        }

        val inflater = LayoutInflater.from(this)
        for (room in rooms) {
            val roomView = inflater.inflate(R.layout.item_delete_screening_room, roomsContainer, false)
            val nameTextView = roomView.findViewById<TextView>(R.id.roomName)
            val deleteButton = roomView.findViewById<Button>(R.id.deleteRoomButton)

            nameTextView.text = room.name
            deleteButton.setOnClickListener {
                viewModel.deleteScreeningRoom(room.name)
            }

            roomsContainer.addView(roomView)
        }

        roomsContainer.invalidate()
        roomsContainer.requestLayout()
    }
}