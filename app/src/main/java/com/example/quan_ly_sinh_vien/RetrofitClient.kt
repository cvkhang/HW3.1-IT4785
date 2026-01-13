package com.example.quan_ly_sinh_vien

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
  private const val BASE_URL = "https://lebavui.io.vn/"

  val instance: StudentApiService by lazy {
    val retrofit =
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

    retrofit.create(StudentApiService::class.java)
  }
}
