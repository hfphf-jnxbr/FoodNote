package com.example.foodnote.data.databaseRoom.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodnote.data.model.recipes.RecipesListImages
import com.squareup.moshi.Json

@Entity(tableName = "recipesFavorite")
data class EntitiesRecipes(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "image") val image: String,
   // @ColumnInfo(name = "images") val images: RecipesListImages,
    //@ColumnInfo(name = "ingredientLines") val ingredientLines: List<String>,
    @ColumnInfo(name = "calories") val calories: String,
    @ColumnInfo(name = "totalTime") val totalTime: String,
    @ColumnInfo(name = "totalWeight") val totalWeight: String
)
