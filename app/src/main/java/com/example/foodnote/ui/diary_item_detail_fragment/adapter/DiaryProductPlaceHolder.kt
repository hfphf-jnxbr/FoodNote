package com.example.foodnote.ui.diary_item_detail_fragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.databinding.DiaryProductItemBinding

class DiaryProductPlaceHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = DiaryProductItemBinding.bind(item)
    private val context = binding.root.context
    fun bind(item: FoodDto) = with(binding) {

    }
}