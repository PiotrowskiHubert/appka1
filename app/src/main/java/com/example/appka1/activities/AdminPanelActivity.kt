package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R
import com.example.appka1.activities.adminPanel.AddMovieActivity
import com.example.appka1.activities.adminPanel.AddScreeningRoomActivity
import com.example.appka1.activities.adminPanel.AddShowingActivity
import com.example.appka1.activities.adminPanel.DeleteMovieActivity
import com.example.appka1.activities.adminPanel.DeleteScreeningRoomActivity
import com.example.appka1.activities.adminPanel.DeleteShowingActivity

class AdminPanelActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val addMovieButton = findViewById<Button>(R.id.btnAddMovie)
        val deleteMovieButton = findViewById<Button>(R.id.btnDeleteMovie)
        val addScreeningRoomButton = findViewById<Button>(R.id.btnAddScreeningRoom)
        val deleteScreeningRoomButton = findViewById<Button>(R.id.btnDeleteScreeningRoom)
        val addShowingButton = findViewById<Button>(R.id.btnAddShowing)
        val deleteShowingButton = findViewById<Button>(R.id.btnDeleteShowing)

        addMovieButton.setOnClickListener {
            val intent = Intent(this, AddMovieActivity::class.java)
            startActivity(intent)
        }

        deleteMovieButton.setOnClickListener {
            val intent = Intent(this, DeleteMovieActivity::class.java)
            startActivity(intent)
        }

        addScreeningRoomButton.setOnClickListener {
            val intent = Intent(this, AddScreeningRoomActivity::class.java)
            startActivity(intent)
        }

        deleteScreeningRoomButton.setOnClickListener {
            val intent = Intent(this, DeleteScreeningRoomActivity::class.java)
            startActivity(intent)
        }

        addShowingButton.setOnClickListener {
            val intent = Intent(this, AddShowingActivity::class.java)
            startActivity(intent)
        }

        deleteShowingButton.setOnClickListener {
            val intent = Intent(this, DeleteShowingActivity::class.java)
            startActivity(intent)
        }

    }
}