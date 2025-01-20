package com.example.appka1.activities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appka1.R
import com.example.appka1.models.Showing

class MoviesAdapter(
    private val context: Context,
    private val groupedShowings: Map<String, List<Showing>>
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    // Widok dla każdego elementu
    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieTitle: TextView = view.findViewById(R.id.movieTitle)
        val showingsContainer: LinearLayout = view.findViewById(R.id.showingsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieTitle = groupedShowings.keys.elementAt(position)
        val showings = groupedShowings[movieTitle] ?: emptyList()

        holder.movieTitle.text = movieTitle
        holder.showingsContainer.removeAllViews()

        // Tworzenie przycisków dla każdej godziny seansu
        for (showing in showings) {
            val button = Button(context).apply {
                text = showing.startTime
                textSize = 14f
                setTextColor(context.getColor(android.R.color.white))
                setBackgroundResource(R.drawable.rounded_button) // Ustawiamy zaokrąglony przycisk
                setPadding(16, 8, 16, 8)
                setOnClickListener {
                    val intent = Intent(context, SeatsActivity::class.java).apply {
                        putExtra("SHOWING_ID", showing.id)
                    }
                    context.startActivity(intent)
                }
            }
            holder.showingsContainer.addView(button)
        }

    }

    override fun getItemCount(): Int = groupedShowings.size
}

