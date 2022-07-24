package com.example.foodnote.data.model.recipes

import com.squareup.moshi.Json

data class RecipesList(
	@field:Json(name = "hits") val hints: List<RecipesX>
)

