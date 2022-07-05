package com.example.foodnote.data.datasource.calorire_datasource.firebase

import android.util.Log
import com.example.foodnote.data.model.DiaryItem
import com.google.firebase.firestore.FirebaseFirestore


class FireBaseCalorieDataSourceImpl(private val db: FirebaseFirestore) : FirebaseCalorieDataSource {
    override fun saveDiaryItem(diaryItem: DiaryItem, idUser: String, date: String) {
        db.collection("Diary")
            .add(diaryItem)
            .addOnSuccessListener { documentReference ->
                Log.d("DB_SAVE", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DB_SAVE_ERROR", "Error adding document", e)
            }

    }

    override fun getDiaryCollection(idUser: String, date: String): MutableList<DiaryItem> {
        db.collection("Diary")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        return mutableListOf()
    }

}