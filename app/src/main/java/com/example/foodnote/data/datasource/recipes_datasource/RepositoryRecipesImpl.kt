package com.example.foodnote.data.datasource.recipes_datasource

import com.example.foodnote.data.base.RetrofitRecipesImpl
import com.example.foodnote.data.model.recipes.RecipesList

class RepositoryRecipesImpl(private val retrofitRecipesImpl: RetrofitRecipesImpl) :
    RepositoryRecipes {
    override suspend fun searchRecipes(product: String): RecipesList {
        val l = retrofitRecipesImpl.getService().getRecipesByName(nameIngr = product)
        return l
    }
}