package com.example.quan_ly_sinh_vien

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Model class đại diện cho một sinh viên */
@Parcelize
data class Student(
        val id: String, // MSSV
        var name: String, // Họ tên
        var phone: String, // Số điện thoại
        var address: String // Địa chỉ
) : Parcelable
