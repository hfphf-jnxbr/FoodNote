package com.example.foodnote.data.model.food

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodDto(
    val image: String,
    val name: String,
    val kiloCalories: Int,
    val protein: Int,
    val fat: Int,
    val carbohydrate: Int,
    var count: Int = 0,
    var docId: String? = null
) : Parcelable {
    fun incCount(): FoodDto {
        ++count
        return this
    }

    fun decCount(): FoodDto {
        --count
        return this
    }
}