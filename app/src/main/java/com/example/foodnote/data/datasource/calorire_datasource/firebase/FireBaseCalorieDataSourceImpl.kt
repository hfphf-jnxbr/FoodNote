package com.example.foodnote.data.datasource.calorire_datasource.firebase

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.FoodFireBase
import com.example.foodnote.utils.toFoodDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*


class FireBaseCalorieDataSourceImpl(private val db: FirebaseFirestore) : FirebaseCalorieDataSource {
    private companion object {
        const val PRODUCT_COLLECTION_NAME = "Products"
        const val DIARY_DOCUMENT_NAME = "Diary"
        const val DIARY_ITEM_COLLECTION_NAME = "DiaryItem"
    }

    override fun saveDiaryItem(diaryItem: DiaryItem, foodItem: FoodDto?): Flow<AppState<String>> {
        return flow {
            diaryItem.idUser?.let {
                emit(AppState.Loading())
                val collection = db.collection(diaryItem.idUser)
                    .document(DIARY_DOCUMENT_NAME)
                    .collection(DIARY_ITEM_COLLECTION_NAME)
                    .document(diaryItem.dbId ?: UUID.randomUUID().toString())

                collection.set(diaryItem)
                    .await()
                if (foodItem != null) {
                    collection.collection(PRODUCT_COLLECTION_NAME)
                        .document(UUID.randomUUID().toString())
                        .set(foodItem)
                }
                emit(AppState.Success("Success"))
            }
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    override fun getDiaryCollection(
        idUser: String,
        date: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return flow<AppState<MutableList<DiaryItem>>> {
            emit(AppState.Loading())
            val path = "/$idUser/$DIARY_DOCUMENT_NAME/$DIARY_ITEM_COLLECTION_NAME"
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

    override fun getSavedFoodCollection(
        idUser: String,
        diaryId: String
    ): Flow<AppState<List<FoodDto>>> {
        return flow<AppState<List<FoodDto>>> {
            val path =
                "/$idUser/$DIARY_DOCUMENT_NAME/$DIARY_ITEM_COLLECTION_NAME/$diaryId/$PRODUCT_COLLECTION_NAME"
            emit(AppState.Loading())
            val result = db
                .collection(path)
                .get()
                .await()
            val items = result.toObjects(FoodFireBase::class.java).map {
                it.toFoodDto()
            }
            emit(AppState.Success(items))
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }


}