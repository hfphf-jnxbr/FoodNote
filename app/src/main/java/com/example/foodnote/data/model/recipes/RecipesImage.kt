package com.example.foodnote.data.model.recipes

import com.squareup.moshi.Json

data class RecipesImage(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "width") val width: String,
    @field:Json(name = "height") val height: String,
)