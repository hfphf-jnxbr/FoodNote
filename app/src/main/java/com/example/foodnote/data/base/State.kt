package com.example.foodnote.data.base

import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult

data class SampleState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isSuccess: Boolean = false,
    val diaryList: MutableList<DiaryItem> = ArrayList(),
    var diaryItem: DiaryItem? = null,
    val calorie: Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>? = null,
    val foodDtoItems: List<FoodDto> = ArrayList(),
    var foodDtoItem: FoodDto? = null,
    var totalFoodResult: TotalFoodResult? = null
)