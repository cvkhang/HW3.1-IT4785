package com.example.quan_ly_sinh_vien

import retrofit2.Call
import retrofit2.http.GET

interface StudentApiService {
  @GET("students") fun getStudents(): Call<List<Student>>
}
