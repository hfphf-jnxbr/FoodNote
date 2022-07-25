package com.example.foodnote.data.model.recipes

import com.squareup.moshi.Json

data class RecipesX(
    @field:Json(name = "recipe") val recipe: Recipes
)