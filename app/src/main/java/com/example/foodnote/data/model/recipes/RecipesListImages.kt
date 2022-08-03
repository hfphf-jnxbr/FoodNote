package com.example.foodnote.data.model.recipes

import com.squareup.moshi.Json

data class RecipesListImages(
    @field:Json(name = "THUMBNAIL") val THUMBNAIL: RecipesImage,
    @field:Json(name = "SMALL") val SMALL: RecipesImage,
    @field:Json(name = "REGULAR") val REGULAR: RecipesImage,
)