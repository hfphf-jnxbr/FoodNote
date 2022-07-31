package com.example.foodnote.data.base

import com.example.foodnote.BuildConfig
import com.example.foodnote.data.datasource.api.ApiService
import com.example.foodnote.di.TIME_OUT_CONNECT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitImpl {

    fun getService(): ApiService {
        return createRetrofit().create(ApiService::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_TRANSLATE_SERVICE)
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
        httpClient.connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
        httpClient.readTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .url(
                    original.url.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.TRANSLATE_SERVICE_API_KEY)
                        .build()
                )
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

}