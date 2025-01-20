package com.example.appka1.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Historia kina
        val historyTextView: TextView = findViewById(R.id.history_text)
        val historyText = "WroCinema to nowoczesne kino w sercu Wrocławia, które łączy pasję do filmów z wyjątkową atmosferą, oferując szeroką gamę seansów, od najnowszych hitów po kameralne projekcje artystyczne."
        historyTextView.text = historyText

        // Adres kina
        val addressTextView: TextView = findViewById(R.id.address_text)
        val addressText = "Adres: ul. Przykładowa 123, 50-123 Wrocław"
        addressTextView.text = addressText

        // Kontakt telefoniczny i mailowy
        val contactTextView: TextView = findViewById(R.id.contact_text)
        val contactText = "Telefon:+48 123 456 789\nEmail:kontakt@wrocinema.pl"
        contactTextView.text = contactText
    }
}
