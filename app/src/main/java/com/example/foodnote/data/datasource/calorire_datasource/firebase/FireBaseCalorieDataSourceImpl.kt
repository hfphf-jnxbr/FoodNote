package com.example.foodnote.data.datasource.calorire_datasource.firebase

import android.util.Log
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*


class FireBaseCalorieDataSourceImpl(private val db: FirebaseFirestore) : FirebaseCalorieDataSource {
    override fun saveDiaryItem(diaryItem: DiaryItem): DiaryItem {
        diaryItem.idUser?.let {
            db.collection(diaryItem.idUser)
                .document("Diary")
                .collection("DiaryItem")
                .document(UUID.randomUUID().toString())
                .set(diaryItem)
                .addOnSuccessListener {
                    Log.d("DB_SAVE", "DocumentSnapshot add")
                }
                .addOnFailureListener { e ->
                    Log.w("DB_SAVE_ERROR", "Error adding document", e)
                }
        }

        return diaryItem
    }

    override fun getDiaryCollection(
        idUser: String,
        date: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return flow<AppState<MutableList<DiaryItem>>> {
            emit(AppState.Loading())
            val path = "/$idUser/Diary/DiaryItem"
            val result = db
                .collection(path)
                .whereEqualTo("date", date)
                .get()
                .await()
            val items = result.toObjects(DiaryItem::class.java)
            emit(AppState.Success(items))
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

}