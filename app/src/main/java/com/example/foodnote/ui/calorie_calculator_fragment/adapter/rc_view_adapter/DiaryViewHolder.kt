package com.example.foodnote.ui.calorie_calculator_fragment.adapter.rc_view_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.databinding.DiaryItemBinding

class DiaryViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = DiaryItemBinding.bind(item)
    private val context = binding.root.context
    fun bind(item: DiaryItem, listener: ItemClickListener) = with(binding) {
        diaryTitleTextView.text = item.name
        diaryValueCaloriesTextView.text = item.calories.toString()
        diaryTimeTextView.text = item.time
        root.setOnClickListener {
            listener.navigateToDiaryDetail(item)
        }
    }
}