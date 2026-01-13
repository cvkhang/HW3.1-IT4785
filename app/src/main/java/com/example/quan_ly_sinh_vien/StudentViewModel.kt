package com.example.quan_ly_sinh_vien

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel : androidx.lifecycle.ViewModel() {

  // LiveData chứa danh sách sinh viên
  private val _students = MutableLiveData<List<Student>>()
  val students: LiveData<List<Student>> = _students

  // Danh sách gốc để filter
  private var originalList: List<Student> = emptyList()

  init {
    loadStudents()
  }

  /** Load danh sách sinh viên từ API */
  private fun loadStudents() {
    RetrofitClient.instance
            .getStudents()
            .enqueue(
                    object : retrofit2.Callback<List<Student>> {
                      override fun onResponse(
                              call: retrofit2.Call<List<Student>>,
                              response: retrofit2.Response<List<Student>>
                      ) {
                        if (response.isSuccessful) {
                          originalList = response.body() ?: emptyList()
                          _students.value = originalList
                        }
                      }

                      override fun onFailure(call: retrofit2.Call<List<Student>>, t: Throwable) {
                        // Handle error (e.g., log or show message)
                        t.printStackTrace()
                      }
                    }
            )
  }

  /** Tìm kiếm sinh viên */
  fun searchStudents(query: String) {
    if (query.isEmpty()) {
      _students.value = originalList
    } else {
      val filtered =
              originalList.filter {
                it.hoten.contains(query, ignoreCase = true) ||
                        it.mssv.contains(query, ignoreCase = true)
              }
      _students.value = filtered
    }
  }
}
