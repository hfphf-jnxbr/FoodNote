package com.example.foodnote.data.base

import com.example.foodnote.data.model.DiaryItem

data class SampleState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val diaryList: ArrayList<DiaryItem> = ArrayList(),
    val calorie: Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>? = null
)