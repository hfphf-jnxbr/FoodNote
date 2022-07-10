package com.example.foodnote.ui.calorie_calculator_fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.R
import com.example.foodnote.data.model.DiaryItem

class CalorieCalculatorAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<DiaryViewHolder>() {

    private var list = mutableListOf<DiaryItem>()
    fun setItem(newList: MutableList<DiaryItem>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        return DiaryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.diary_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}