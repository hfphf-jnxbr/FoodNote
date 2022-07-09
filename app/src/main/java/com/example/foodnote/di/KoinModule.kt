package com.example.foodnote.di

import android.content.Context
import androidx.room.Room
import com.example.foodnote.data.base.RetrofitImpl
import com.example.foodnote.data.databaseRoom.DataBase
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.example.foodnote.ui.noteBook.viewModel.ViewModelConstructorFragment
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

    single(named(DATA_BASE)) { (context : Context) ->
        Room.databaseBuilder(context, DataBase::class.java, DATA_BASE_NAME).build().dataBase()
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

val noteBookModule = module {
    viewModel { ViewModelConstructorFragment() }
}