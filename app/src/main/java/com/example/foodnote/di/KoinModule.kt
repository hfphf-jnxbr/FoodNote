package com.example.foodnote.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.foodnote.data.base.RetrofitImpl
import com.example.foodnote.data.base.RetrofitRecipesImpl
import com.example.foodnote.data.base.firebase.FireBaseDataSourceImpl
import com.example.foodnote.data.base.firebase.FirebaseDataSource
import com.example.foodnote.data.databaseRoom.DataBase
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasource
import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasourceImpl
import com.example.foodnote.data.datasource.recipes_datasource.RepositoryRecipesImpl
import com.example.foodnote.data.interactor.calorie_interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.interactor.calorie_interactor.CalorieCalculatorInteractorImpl
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractor
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractorImpl
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractor
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractorImpl
import com.example.foodnote.data.repository.calorie_repository.CalorieRepository
import com.example.foodnote.data.repository.calorie_repository.CalorieRepositoryImpl
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl
import com.example.foodnote.data.repository.diary_item_detail_repository.DiaryItemDetailRepository
import com.example.foodnote.data.repository.diary_item_detail_repository.DiaryItemDetailRepositoryImpl
import com.example.foodnote.data.repository.settings_repository.SettingRepository
import com.example.foodnote.data.repository.settings_repository.SettingRepositoryImpl
import com.example.foodnote.ui.auth_fragment.viewModel.AuthViewModel
import com.example.foodnote.ui.base.viewModel.MainViewModel
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.composeUi.ViewModelWaterFragmentCompose
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.example.foodnote.ui.diary_item_detail_fragment.viewModel.DiaryItemDetailViewModel
import com.example.foodnote.ui.noteBook.viewModel.ViewModelConstructorFragment
import com.example.foodnote.ui.noteBook.viewModel.ViewModelNotesFragment
import com.example.foodnote.ui.recipes_fragment.RecipesViewModel
import com.example.foodnote.ui.recipes_fragment.ViewModelDonatViewCompose
import com.example.foodnote.ui.settings_fragment.viewModel.SettingsViewModel
import com.example.foodnote.ui.splash_screen_fragment.viewModel.SplashScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    single(named(NAME_DATASOURCE_REMOTE)) { RetrofitImpl() }
    // Получаем сервис
    single(named(NAME_DATASOURCE_REMOTE_SERVICE)) {
        get<RetrofitImpl>(qualifier = named(NAME_DATASOURCE_REMOTE)).getService()
    }

    // FireStore db
    single(named(NAME_DATASOURCE_FIREBASE)) {
        FirebaseFirestore.getInstance()
    }

    single(named(DATA_BASE)) { (context: Context) ->
        Room.databaseBuilder(context, DataBase::class.java, DATA_BASE_NAME).build().dataBase()
    }

}

val activityMainScreenModule = module {
    viewModel {
        MainViewModel(get(named(NAME_PREF_APP_REPOSITORY)))
    }
}
val splashScreenModule = module {
    viewModel {
        SplashScreenViewModel(get(named(NAME_PREF_APP_REPOSITORY)))
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
    single<UserPreferencesRepository>(named(NAME_PREF_APP_REPOSITORY)) {
        UserPreferencesRepositoryImpl(get(named(NAME_DATA_STORE_PREF)))
    }
}
val calorieCalculatorScreenModule = module {
    factory<FirebaseDataSource> {
        FireBaseDataSourceImpl(get(named(NAME_DATASOURCE_FIREBASE)))
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

    viewModel { ViewModelWaterFragmentCompose() }
    viewModel { ViewModelDonatViewCompose() }
}

val authScreenModule = module {
    viewModel {
        AuthViewModel(get(named(NAME_PREF_APP_REPOSITORY)))
    }
}


val diaryItemDetailScreenModule = module {
    factory<FirebaseDataSource> {
        FireBaseDataSourceImpl(get(named(NAME_DATASOURCE_FIREBASE)))
    }

    factory<DiaryItemDetailDatasource> {
        DiaryItemDetailDatasourceImpl(get(named(NAME_DATASOURCE_REMOTE_SERVICE)))
    }

    factory<DiaryItemDetailRepository> {
        DiaryItemDetailRepositoryImpl(get(), get())
    }

    factory<DiaryItemDetailInteractor> {
        DiaryItemDetailInteractorImpl(get())
    }

    viewModel {
        DiaryItemDetailViewModel(get(named(NAME_PREF_APP_REPOSITORY)), get())
    }
}

val noteBookModule = module {
    viewModel {
        ViewModelConstructorFragment()
    }

    viewModel(named(VIEW_MODEL_NOTES)) { (dao: DaoDB) ->
        ViewModelNotesFragment(dao)
    }
}

val recipesModule = module {
    single(named(DATASOURCE_RECIPES)) { RetrofitRecipesImpl() }

    factory(named(REPOSITORY_RECIPES)) {
        RepositoryRecipesImpl(get(named(DATASOURCE_RECIPES)))
    }

    viewModel {
        RecipesViewModel(
            get(named(NAME_PREF_APP_REPOSITORY)), get(named(REPOSITORY_RECIPES)))
    }
}

val settingScreenModule = module {
    factory<FirebaseDataSource> {
        FireBaseDataSourceImpl(get(named(NAME_DATASOURCE_FIREBASE)))
    }

    factory<SettingRepository> {
        SettingRepositoryImpl(get())
    }

    factory<SettingInteractor> {
        SettingInteractorImpl(get())
    }

    viewModel {
        SettingsViewModel(get(named(NAME_PREF_APP_REPOSITORY)), get())
    }
}