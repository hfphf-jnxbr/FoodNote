package com.example.foodnote.data.base

import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto

data class SampleState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isSuccess: Boolean = false,
    val diaryList: MutableList<DiaryItem> = ArrayList(),
    val lastAddItem: DiaryItem? = null,
    val calorie: Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>? = null,
    val foodDtoItems: List<FoodDto> = ArrayList()
)