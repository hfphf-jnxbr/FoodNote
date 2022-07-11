package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.food.FoodDto

interface DiaryItemDetailInteractor {
    fun searchFood(name: String): List<FoodDto>
}