package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private lateinit var edtStudentId: EditText
    private lateinit var edtStudentName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    
    // Lưu sinh viên đang được chọn để cập nhật
    private var selectedStudent: Student? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        
        setupRecyclerView()
        
        setupListeners()
        
        btnUpdate.isEnabled = false
    }

    private fun initViews() {
        edtStudentId = findViewById(R.id.edtStudentId)
        edtStudentName = findViewById(R.id.edtStudentName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerViewStudents)
    }
    
    /**
     * Thiết lập RecyclerView
     */
    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            students = mutableListOf(),
            onItemClick = { student ->
                // Khi click vào một sinh viên, hiển thị thông tin lên EditText
                selectedStudent = student
                edtStudentId.setText(student.id)
                edtStudentName.setText(student.name)
                
                // Vô hiệu hóa ô nhập MSSV khi đang chỉnh sửa
                edtStudentId.isEnabled = false
                
                // Kích hoạt nút Update, vô hiệu hóa nút Add
                btnUpdate.isEnabled = true
                btnAdd.isEnabled = false
            },
            onDeleteClick = { student ->
                // Xóa sinh viên khỏi danh sách
                studentAdapter.removeStudent(student)
                Toast.makeText(this, "Đã xóa ${student.name}", Toast.LENGTH_SHORT).show()
                
                // Nếu đang chỉnh sửa sinh viên này, reset form
                if (selectedStudent == student) {
                    resetForm()
                }
            }
        )
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
        }
    }
    
    /**
     * Thiết lập các sự kiện cho buttons
     */
    private fun setupListeners() {
        // Nút Add - Thêm sinh viên mới
        btnAdd.setOnClickListener {
            val studentId = edtStudentId.text.toString().trim()
            val studentName = edtStudentName.text.toString().trim()
            
            // Kiểm tra dữ liệu đầu vào
            if (studentId.isEmpty() || studentName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Tạo và thêm sinh viên mới
            val newStudent = Student(studentId, studentName)
            studentAdapter.addStudent(newStudent)
            
            // Reset form
            resetForm()
            
            Toast.makeText(this, "Đã thêm sinh viên mới", Toast.LENGTH_SHORT).show()
        }
        
        // Nút Update - Cập nhật thông tin sinh viên
        btnUpdate.setOnClickListener {
            val newName = edtStudentName.text.toString().trim()
            
            if (newName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            selectedStudent?.let { student ->
                // Cập nhật tên sinh viên
                studentAdapter.updateStudent(student, newName)
                
                // Reset form
                resetForm()
                
                Toast.makeText(this, "Đã cập nhật thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Reset form về trạng thái ban đầu
     */
    private fun resetForm() {
        edtStudentId.setText("")
        edtStudentName.setText("")
        edtStudentId.isEnabled = true
        btnAdd.isEnabled = true
        btnUpdate.isEnabled = false
        selectedStudent = null
    }
}
