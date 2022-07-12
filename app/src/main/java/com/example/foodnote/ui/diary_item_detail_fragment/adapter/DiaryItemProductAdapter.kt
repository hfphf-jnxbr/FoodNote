package com.example.foodnote.ui.diary_item_detail_fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.R
import com.example.foodnote.data.model.food.FoodDto

class DiaryItemProductAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<DiaryProductPlaceHolder>() {
    private var list: List<FoodDto> = ArrayList<FoodDto>(INIT_CAPACITY)

    fun setItem(list: List<FoodDto>) {
        this.list = list
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryProductPlaceHolder {
        return DiaryProductPlaceHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.diary_product_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: DiaryProductPlaceHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private companion object {
        const val INIT_CAPACITY = 50
    }
}