package com.example.appka1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appka1.Promotion
import com.example.appka1.PromotionsAdapter
import com.example.appka1.R

class PromotionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotions)
        // Przykładowa lista promocji
        val promotions = listOf(
            Promotion(
                title = "Zestaw kinowy za 25 zł",
                description = "Kup duży popcorn i napój w promocyjnej cenie!"
            ),
            Promotion(
                title = "Wieczór dla par",
                description = "Dwa bilety w cenie jednego na seanse po 20:00!"
            ),
            Promotion(
                title = "Poranki dla dzieci",
                description = "Bilety tańsze o 30% na seanse do godziny 12:00."
            )
        )

        // Inicjalizacja RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.promotionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) // Ustawienie layout managera
        recyclerView.adapter = PromotionsAdapter(promotions)  // Przypisanie adaptera
    }
}