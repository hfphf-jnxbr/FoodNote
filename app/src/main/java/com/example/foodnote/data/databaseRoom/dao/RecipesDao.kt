package com.example.foodnote.data.databaseRoom.dao

import androidx.room.*
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesFood
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesPaint
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesStandard
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipesFavorite")
    fun getAllRecipesFavorite() : List<EntitiesRecipes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipesFavorite(entitiesRecipes : EntitiesRecipes)

    @Delete
    fun removeRecipesFavorite(entitiesRecipes : EntitiesRecipes)
}