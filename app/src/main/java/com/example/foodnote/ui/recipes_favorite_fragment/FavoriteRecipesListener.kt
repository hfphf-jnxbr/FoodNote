package com.example.foodnote.ui.recipes_favorite_fragment

import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes

interface FavoriteRecipesListener {
    fun onClickRecipes(recipes: EntitiesRecipes)
}