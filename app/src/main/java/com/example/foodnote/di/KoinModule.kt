package com.example.foodnote.di

import com.example.foodnote.data.base.RetrofitImpl
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
    factory {
        FirebaseFirestore.getInstance()
    }
    viewModel {
        CalorieCalculatorViewModel()
    }
}