package com.example.appka1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PromotionsAdapter(private val promotions: List<Promotion>) :
    RecyclerView.Adapter<PromotionsAdapter.ViewHolder>() {

    // ViewHolder - przechowuje widoki dla elementów listy
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val promotionTitle: TextView = view.findViewById(R.id.promotionTitle)
        val promotionDescription: TextView = view.findViewById(R.id.promotionDescription)
    }

    // Tworzy nowe widoki (wywoływane przez layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promotion_text, parent, false) // Ładowanie layoutu elementu listy
        return ViewHolder(view)
    }

    // Wiąże dane z widokiem
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promotion = promotions[position]
        holder.promotionTitle.text = promotion.title
        holder.promotionDescription.text = promotion.description
    }

    // Zwraca liczbę elementów na liście
    override fun getItemCount(): Int = promotions.size
}
