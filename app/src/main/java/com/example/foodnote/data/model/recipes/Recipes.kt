package com.example.foodnote.data.model.recipes

import com.squareup.moshi.Json

data class Recipes(
    @field:Json(name = "label") val label: String,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "images") val images: RecipesListImages,
    @field:Json(name = "ingredientLines") val ingredientLines: List<String>,
    @field:Json(name = "calories") val calories: String,
    @field:Json(name = "totalTime") val totalTime: String,
    @field:Json(name = "totalWeight") val totalWeight: String,
)