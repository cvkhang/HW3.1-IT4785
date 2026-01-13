package com.example.quan_ly_sinh_vien

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/** Model class đại diện cho một sinh viên */
@Parcelize
data class Student(
        @SerializedName("mssv") val mssv: String,
        @SerializedName("hoten") val hoten: String,
        @SerializedName("ngaysinh") val ngaysinh: String,
        @SerializedName("email") val email: String,
        @SerializedName("thumbnail") val thumbnail: String
) : Parcelable {
        val fullThumbnailUrl: String
                get() {
                        return if (thumbnail.startsWith("http")) {
                                thumbnail
                        } else {
                                "https://lebavui.io.vn$thumbnail"
                        }
                }
}
