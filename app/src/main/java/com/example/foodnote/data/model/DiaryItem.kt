package com.example.foodnote.data.model

import android.os.Parcelable
import com.example.foodnote.data.model.food.FoodDto
import kotlinx.parcelize.Parcelize


@Parcelize
data class DiaryItem(
    val name: String? = null,
    val calories: Long? = null,
    val time: String? = null,
    val date: String? = null,
    val idUser: String? = null,
    val dbId: String? = null,
    val foodList: List<FoodDto>? = null
) : Parcelable