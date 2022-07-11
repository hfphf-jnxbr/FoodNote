package com.example.foodnote.data.datasource.api

import com.example.foodnote.data.model.food.Food
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /**
     * Получение данных о продукте по названию
     */
    @GET("food-database/v2/parser")
    suspend fun getProductByName(
        @Query("ingr") ingr: String,
        @Query("nutrition-type") type: String = "cooking"
    ): Food
}