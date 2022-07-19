package com.example.foodnote.ui.diary_item_detail_fragment.adapter

import com.example.foodnote.data.model.food.FoodDto

interface ItemClickListener {
    fun addProduct(item: FoodDto, pos: Int)
    fun deleteProduct(item: FoodDto, pos: Int)
}