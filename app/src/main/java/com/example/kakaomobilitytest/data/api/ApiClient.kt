package com.example.kakaomobilitytest.data.api

import com.example.kakaomobilitytest.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://taxi-openapi.sandbox.onkakao.net/api/v1/coding-assignment/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: KakaoApiService by lazy {
        retrofit.create(KakaoApiService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", BuildConfig.KAKAO_API_KEY)
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }
}
