package com.example.foodnote.data.databaseRoom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotes

@Dao
interface DaoDB {
    @Query("SELECT * FROM note")
    fun getAllNotes() : List<EntitiesNotes>

    @Query("DELETE FROM note WHERE id LIKE :id")
    fun deleteNote(id : Int) : Int

    @Query("DELETE FROM note")
    fun deleteAll() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListNotes(list : List<EntitiesNotes>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(day : EntitiesNotes)
}