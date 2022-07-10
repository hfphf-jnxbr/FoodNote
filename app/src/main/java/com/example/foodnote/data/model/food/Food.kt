package com.example.foodnote.data.model.food

import com.squareup.moshi.Json

data class Food(
	@field:Json(name = "label") val label: String,
	@field:Json(name = "parsed") val parsed: List<ParsedItem>,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "nutrients") val nutrients: Nutrients
)

