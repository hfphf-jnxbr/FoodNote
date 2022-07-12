package com.example.foodnote.utils

import com.example.foodnote.data.model.food.Food
import com.example.foodnote.data.model.food.FoodDto

fun Food.toFoodListDto(): List<FoodDto> {
    val foods = this.parsed.map {
        it.food.toFoodDto()
    }

    return foods
}

fun Food.toFoodDto(): FoodDto {
    return FoodDto(
        this.image ?: "",
        this.label ?: "",
        this.nutrients?.enercal ?: 0.0,
        this.nutrients?.procnt ?: 0.0,
        this.nutrients?.fat ?: 0.0,
        this.nutrients?.chocdf ?: 0.0
    )
}