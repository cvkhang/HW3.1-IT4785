package com.example.quan_ly_sinh_vien

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** ViewModel để quản lý dữ liệu sinh viên Chia sẻ dữ liệu giữa các Fragment */
class StudentViewModel : ViewModel() {

  // LiveData chứa danh sách sinh viên
  private val _students = MutableLiveData<MutableList<Student>>()
  val students: LiveData<MutableList<Student>> = _students

  init {
    // Khởi tạo dữ liệu mẫu
    initSampleData()
  }

  /** Khởi tạo dữ liệu mẫu */
  private fun initSampleData() {
    _students.value =
            mutableListOf(
                    Student("20200001", "Nguyễn Văn A", "0123456789", "Hà Nội"),
                    Student("20200002", "Trần Thị B", "0987654321", "Hồ Chí Minh"),
                    Student("20200003", "Lê Văn C", "0369852147", "Đà Nẵng"),
                    Student("20200004", "Phạm Thị D", "0741852963", "Hải Phòng"),
                    Student("20200005", "Hoàng Văn E", "0258963147", "Cần Thơ")
            )
  }

  /** Lấy danh sách sinh viên */
  fun getStudents(): List<Student> {
    return _students.value ?: emptyList()
  }

  /** Thêm sinh viên mới */
  fun addStudent(student: Student) {
    val currentList = _students.value ?: mutableListOf()
    currentList.add(student)
    _students.value = currentList
  }

  /** Cập nhật thông tin sinh viên */
  fun updateStudent(studentId: String, name: String, phone: String, address: String) {
    val currentList = _students.value ?: return
    val index = currentList.indexOfFirst { it.id == studentId }

    if (index != -1) {
      currentList[index] = Student(studentId, name, phone, address)
      _students.value = currentList
    }
  }

  /** Xóa sinh viên */
  fun removeStudent(student: Student) {
    val currentList = _students.value ?: return
    currentList.remove(student)
    _students.value = currentList
  }

  /** Tìm sinh viên theo ID */
  fun findStudentById(studentId: String): Student? {
    return _students.value?.find { it.id == studentId }
  }
}
