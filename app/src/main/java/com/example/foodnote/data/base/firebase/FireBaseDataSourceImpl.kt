package com.example.foodnote.data.base.firebase

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.FoodFireBase
import com.example.foodnote.data.model.profile.Profile
import com.example.foodnote.utils.toFoodDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*


class FireBaseDataSourceImpl(private val db: FirebaseFirestore) : FirebaseDataSource {
    private companion object {
        const val PRODUCT_COLLECTION_NAME = "Products"
        const val DIARY_DOCUMENT_NAME = "Diary"
        const val DIARY_ITEM_COLLECTION_NAME = "DiaryItem"
        const val PROFILE_DOCUMENT_NAME = "Profile"
    }

    /**
     * Сохранение профиля данных
     *
     * @param profile профиль пользователя
     * @param idUser идентификатор пользователя
     */
    override fun saveProfileData(profile: Profile, idUser: String): Flow<AppState<String>> {
        return flow {
            emit(AppState.Loading())
            db
                .collection(idUser)
                .document(PROFILE_DOCUMENT_NAME)
                .set(profile)
                .await()
            emit(AppState.Success("Success"))
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Получение профиля
     *
     * @param idUser идентификатор пользователя
     */
    override fun getProfileData(idUser: String): Flow<AppState<Profile?>> {
        return flow {
            emit(AppState.Loading())
            val result = db.collection(idUser).document(PROFILE_DOCUMENT_NAME).get().await()
            val data = result.toObject(Profile::class.java)
            emit(AppState.Success(data))
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Сохранение заметки
     *
     * @param DiaryItem Данные заметки по питанию
     * @param FoodDto Данные о продукте в заметке
     */
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
                    val docId = foodItem.docId ?: UUID.randomUUID().toString()
                    foodItem.docId = docId
                    collection.collection(PRODUCT_COLLECTION_NAME)
                        .document(docId)
                        .set(foodItem)
                }
                emit(AppState.Success("Success"))
            }
        }.catch {
            emit(AppState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Получение коллекции заметок
     *
     * @param idUser String идентификатор пользователя
     * @param date String дата документа
     */
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

    /**
     * Получение сохраненных продуктов
     *
     * @param idUser String идентификатор пользователя
     * @param diaryId String идентификатор документа
     */
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