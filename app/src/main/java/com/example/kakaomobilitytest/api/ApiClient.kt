package com.example.kakaomobilitytest.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    // Retrofit 인스턴스를 필요할 때 한 번만 생성
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://taxi-openapi.sandbox.onkakao.net/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()) // JSON 응답을 파싱할 수 있도록 Gson 변환기 추가
            .client(provideOkHttpClient()) // OkHttpClient 추가 (로깅 및 기타 설정 포함)
            .build()
    }

    // API 서비스 인스턴스 생성
    val apiService: KakaoApiService by lazy {
        retrofit.create(KakaoApiService::class.java)
    }

    // OkHttpClient 설정 (로깅 인터셉터 추가)
    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // 네트워크 요청과 응답을 로그로 확인
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

