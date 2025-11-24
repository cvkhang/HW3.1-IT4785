package com.example.quan_ly_sinh_vien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter cho RecyclerView hiển thị danh sách sinh viên
 */
class StudentAdapter(
    private val students: MutableList<Student>,
    private val onItemClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvId: TextView = itemView.findViewById(R.id.tvStudentId)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvName.text = student.name
        holder.tvId.text = student.id
        
        // Click vào item để chọn và chỉnh sửa
        holder.itemView.setOnClickListener {
            onItemClick(student)
        }
        
        // Click vào nút xóa
        holder.btnDelete.setOnClickListener {
            onDeleteClick(student)
        }
    }

    override fun getItemCount(): Int = students.size
    
    /**
     * Thêm sinh viên mới vào danh sách
     */
    fun addStudent(student: Student) {
        students.add(student)
        notifyItemInserted(students.size - 1)
    }
    
    /**
     * Xóa sinh viên khỏi danh sách
     */
    fun removeStudent(student: Student) {
        val position = students.indexOf(student)
        if (position != -1) {
            students.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    /**
     * Cập nhật thông tin sinh viên
     */
    fun updateStudent(oldStudent: Student, newName: String) {
        val position = students.indexOf(oldStudent)
        if (position != -1) {
            students[position].name = newName
            notifyItemChanged(position)
        }
    }
}
