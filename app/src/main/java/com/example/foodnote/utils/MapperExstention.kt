package com.example.foodnote.utils

import com.example.foodnote.data.model.food.Food
import com.example.foodnote.data.model.food.FoodDto

fun Food.toFoodDto(): FoodDto {
    val foodItem = this.parsed.first().food
    return FoodDto(
        foodItem.image,
        foodItem.label,
        foodItem.nutrients.enercal,
        foodItem.nutrients.procnt,
        foodItem.nutrients.fat,
        foodItem.nutrients.chocdf
    )
}