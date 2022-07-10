package com.example.foodnote.data.model.food

import com.example.foodnote.data.model.Nutrients

data class Food(
	val parsed: List<ParsedItem>,
	val text: String,
	val image: String,
	val foodId: String,
	val categoryLabel: String,
	val label: String,
	val category: String,
	val nutrients: Nutrients
)

