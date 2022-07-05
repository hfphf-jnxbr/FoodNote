package com.example.foodnote.di

import com.example.foodnote.data.base.RetrofitImpl
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FireBaseCalorieDataSourceImpl
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FirebaseCalorieDataSource
import com.example.foodnote.data.interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.interactor.CalorieCalculatorInteractorImpl
import com.example.foodnote.data.repository.CalorieRepository
import com.example.foodnote.data.repository.CalorieRepositoryImpl
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.google.firebase.firestore.FirebaseFirestore
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
        CalorieCalculatorViewModel(get())
    }
}