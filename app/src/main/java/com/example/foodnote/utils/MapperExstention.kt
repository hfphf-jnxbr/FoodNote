package com.example.foodnote.utils

import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.data.model.food.Food
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.FoodFireBase
import com.example.foodnote.data.model.recipes.Recipes
import com.google.gson.Gson

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


fun Recipes.totoEntityRecipes(): EntitiesRecipes {
    return EntitiesRecipes(
        label = this.label ?: "",
        image = this.image ?: "",
        calories = this.calories ?: "",
        totalTime = this.totalTime ?: "",
        totalWeight = this.totalWeight ?: "",
        ingredientLines = Gson().toJson(this.ingredientLines) ?:""
    )
}

/*fun EntitiesRecipes.toRecipes(): Recipes {
    return Recipes(
        label = this.label ?: "",
        image = this.image ?: "",
        calories = this.calories ?: "",
        totalTime = this.totalTime ?: "",
        totalWeight = this.totalWeight ?: "",
        ingredientLines =
    )
}*/
