package com.example.foodnote.data.model.profile

data class Profile(
    val weight: Int? = null,
    val height: Int? = null,
    val age: Int? = null,
    val meta: String? = null,
    val male: Boolean? = null,
    val female: Boolean? = null,
    var calories: Int? = null,
    var protein: Int? = null,
    var fat: Int? = null,
    var carb: Int? = null
)