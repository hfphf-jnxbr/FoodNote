package com.example.foodnote.data.databaseRoom.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class EntitiesNotes(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,

)
