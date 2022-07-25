package com.example.foodnote.data.base

import com.example.foodnote.BuildConfig
import com.example.foodnote.data.datasource.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitRecipesImpl {

    fun getService(): ApiService {
        return createRetrofit().create(ApiService::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory
                    .create()
            )
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .url(
                    original.url.newBuilder()
                        .addQueryParameter("app_id", BuildConfig.EDADIM_APP_ID_RECIPES)
                        .addQueryParameter("app_key", BuildConfig.EDADIM_APP_KEY_RECIPES)
                        .build()
                )
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }
}