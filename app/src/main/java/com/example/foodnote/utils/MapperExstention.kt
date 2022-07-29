package com.example.foodnote.utils

import com.example.foodnote.data.model.food.Food
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.FoodFireBase

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
        this.nutrients?.enercal?.toInt() ?: 0,
        this.nutrients?.procnt?.toInt() ?: 0,
        this.nutrients?.fat?.toInt() ?: 0,
        this.nutrients?.chocdf?.toInt() ?: 0
    )
}


fun FoodFireBase.toFoodDto(): FoodDto {
    return FoodDto(
        this.image ?: "",
        this.name ?: "",
        this.kiloCalories?.toInt() ?: 0,
        this.protein?.toInt() ?: 0,
        this.fat?.toInt() ?: 0,
        this.carbohydrate?.toInt() ?: 0,
        this.count ?: 0,
        this.docId
    )
}