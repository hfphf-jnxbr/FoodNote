package com.example.foodnote.data.model.food

import com.squareup.moshi.Json

data class ParsedItem(
    @field:Json(name = "food") val food: Food
)
