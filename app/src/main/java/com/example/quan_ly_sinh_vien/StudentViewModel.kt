package com.example.quan_ly_sinh_vien

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/** ViewModel để quản lý dữ liệu sinh viên Chia sẻ dữ liệu giữa các Fragment */
class StudentViewModel(application: Application) : AndroidViewModel(application) {

  private val dbHelper = StudentDatabaseHelper(application)

  // LiveData chứa danh sách sinh viên
  private val _students = MutableLiveData<MutableList<Student>>()
  val students: LiveData<MutableList<Student>> = _students

  init {
    // Load data initially
    loadStudents()
  }

  /** Load danh sách sinh viên từ Database vào LiveData */
  private fun loadStudents() {
    _students.value = dbHelper.getAllStudents()
  }

  /** Lấy danh sách sinh viên (helper for adapter init) */
  fun getStudents(): List<Student> {
    return _students.value ?: emptyList()
  }

  /** Thêm sinh viên mới */
  fun addStudent(student: Student) {
    dbHelper.addStudent(student)
    loadStudents()
  }

  /** Cập nhật thông tin sinh viên */
  fun updateStudent(studentId: String, name: String, phone: String, address: String) {
    val student = Student(studentId, name, phone, address)
    dbHelper.updateStudent(student)
    loadStudents()
  }

  /** Xóa sinh viên */
  fun removeStudent(student: Student) {
    dbHelper.deleteStudent(student.id)
    loadStudents()
  }

  /** Tìm sinh viên theo ID (from cached list to avoid DB hit for simple lookup) */
  fun findStudentById(studentId: String): Student? {
    return _students.value?.find { it.id == studentId }
  }
}
