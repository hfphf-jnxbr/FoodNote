package com.example.foodnote.data.model.food

import com.squareup.moshi.Json

data class Food(
    @field:Json(name = "label") val label: String? = null,
    @field:Json(name = "hints") val parsed: List<ParsedItem>,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "nutrients") val nutrients: Nutrients? = null
)

