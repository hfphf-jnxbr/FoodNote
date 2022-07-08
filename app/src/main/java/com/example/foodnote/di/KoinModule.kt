package com.example.foodnote.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.foodnote.data.base.RetrofitImpl
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FireBaseCalorieDataSourceImpl
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FirebaseCalorieDataSource
import com.example.foodnote.data.interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.interactor.CalorieCalculatorInteractorImpl
import com.example.foodnote.data.repository.calorie_repository.CalorieRepository
import com.example.foodnote.data.repository.calorie_repository.CalorieRepositoryImpl
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {


    // Получаем сервис
    single(named(NAME_DATASOURCE_REMOTE)) {
        get<RetrofitImpl>(
            qualifier = named(NAME_DATASOURCE_REMOTE)
        ).getService()
    }

    // FireStore db
    single(named(NAME_DATASOURCE_FIREBASE)) {
        FirebaseFirestore.getInstance()
    }

}
val dataStoreModule = module {
    single(named(NAME_DATA_STORE_PREF)) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidContext().preferencesDataStoreFile(NAME_DATA_STORE_PREF_FILE) }
        )
    }
    factory<UserPreferencesRepository>(named(NAME_PREF_APP_REPOSITORY)) {
        UserPreferencesRepositoryImpl(get(named(NAME_DATA_STORE_PREF)))
    }
}
val calorieCalculatorScreenModule = module {
    factory<FirebaseCalorieDataSource> {
        FireBaseCalorieDataSourceImpl(get(named(NAME_DATASOURCE_FIREBASE)))
    }

    factory<CalorieRepository> {
        CalorieRepositoryImpl(get())
    }

    factory<CalorieCalculatorInteractor> {
        CalorieCalculatorInteractorImpl(get())
    }
    viewModel {
        CalorieCalculatorViewModel(get(), get(named(NAME_PREF_APP_REPOSITORY)))
    }
}