package com.example.foodnote.data.model.food

import com.squareup.moshi.Json

data class Nutrients(
    @field:Json(name = "PROCNT") val procnt: Double,
    @field:Json(name = "ENERC_KCAL") val enercal: Double,
    @field:Json(name = "FAT") val fat: Double,
    @field:Json(name = "CHOCDF") val chocdf: Double,
)