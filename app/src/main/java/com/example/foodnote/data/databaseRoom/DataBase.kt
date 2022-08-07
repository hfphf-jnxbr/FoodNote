package com.example.foodnote.data.databaseRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.databaseRoom.dao.RecipesDao
import com.example.foodnote.data.databaseRoom.entities.*

@Database(entities = [EntitiesNotesPaint::class, EntitiesNotesStandard::class, EntitiesNotesFood::class, EntitiesRecipes::class], version = 4, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun dataBase() : DaoDB
    abstract fun getDBRecipes() : RecipesDao
}