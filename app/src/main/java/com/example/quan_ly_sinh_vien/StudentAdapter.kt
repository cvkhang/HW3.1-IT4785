package com.example.quan_ly_sinh_vien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** Adapter cho RecyclerView hiển thị danh sách sinh viên */
class StudentAdapter(
        private var students: List<Student>,
        private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvId: TextView = itemView.findViewById(R.id.tvStudentId)
        val ivThumbnail: android.widget.ImageView = itemView.findViewById(R.id.ivThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvName.text = student.hoten
        holder.tvId.text = student.mssv

        com.bumptech.glide.Glide.with(holder.itemView.context)
                .load(student.fullThumbnailUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivThumbnail)

        // Click vào item để xem chi tiết
        holder.itemView.setOnClickListener { onItemClick(student) }
    }

    override fun getItemCount(): Int = students.size

    /** Cập nhật toàn bộ danh sách sinh viên */
    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
