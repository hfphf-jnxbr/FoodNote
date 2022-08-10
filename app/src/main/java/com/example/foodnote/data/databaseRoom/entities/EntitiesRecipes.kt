package com.example.foodnote.data.databaseRoom.entities

import androidx.room.*
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


@Entity(tableName = "recipesFavorite")
data class EntitiesRecipes(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "image") val image: String,
   // @ColumnInfo(name = "images") val images: RecipesListImages,
   // @TypeConverters(Converters::class)
    @ColumnInfo(name = "ingredientLines") val ingredientLines:String,
    @ColumnInfo(name = "calories") val calories: String,
    @ColumnInfo(name = "totalTime") val totalTime: String,
    @ColumnInfo(name = "totalWeight") val totalWeight: String
)

/*
@ProvidedTypeConverter
class Converters(private val jsonParser: JsonParser) {
    @TypeConverter
    fun toStringJson(meaning: List<String>) : String {
        return jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<String>>(){}.type
        ) ?: "[]"
    }
    @TypeConverter
    fun fromStringJson(json: String): List<String>{
        return jsonParser.fromJson<ArrayList<String>>(
            json,
            object: TypeToken<ArrayList<String>>(){}.type
        ) ?: emptyList()
    }
}*/
