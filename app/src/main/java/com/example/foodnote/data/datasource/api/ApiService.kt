package com.example.foodnote.data.datasource.api

import com.example.foodnote.data.model.food.Food
import com.example.foodnote.data.model.recipes.RecipesList
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
    /**
     * Получение репептов по названию ингридиента
     */
    @GET("recipes/v2")
    suspend fun getRecipesByName(
        @Query("q") nameIngr: String,
        @Query("type") type: String="any"
    ): RecipesList

}