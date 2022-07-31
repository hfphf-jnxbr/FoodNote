package com.example.foodnote.data.datasource.recipes_datasource

import com.example.foodnote.data.model.recipes.RecipesList

interface RepositoryRecipes {
    suspend fun searchRecipes(product: String): RecipesList
}