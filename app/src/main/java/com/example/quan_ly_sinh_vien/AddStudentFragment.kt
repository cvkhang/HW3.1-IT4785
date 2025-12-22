package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quan_ly_sinh_vien.databinding.FragmentAddStudentBinding

/** Fragment thêm sinh viên mới Sử dụng Data Binding */
class AddStudentFragment : Fragment() {

  private var _binding: FragmentAddStudentBinding? = null
  private val binding
    get() = _binding!!

  // Sử dụng activityViewModels để chia sẻ ViewModel
  private val viewModel: StudentViewModel by activityViewModels()

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    // Inflate layout với Data Binding
    _binding = FragmentAddStudentBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupButtons()
  }

  /** Thiết lập các button */
  private fun setupButtons() {
    binding.btnSave.setOnClickListener { saveStudent() }

    binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
  }

  /** Lưu sinh viên mới */
  private fun saveStudent() {
    val id = binding.edtStudentId.text.toString().trim()
    val name = binding.edtStudentName.text.toString().trim()
    val phone = binding.edtStudentPhone.text.toString().trim()
    val address = binding.edtStudentAddress.text.toString().trim()

    // Validate dữ liệu
    if (id.isEmpty()) {
      Toast.makeText(requireContext(), "Vui lòng nhập MSSV", Toast.LENGTH_SHORT).show()
      binding.edtStudentId.requestFocus()
      return
    }

    if (name.isEmpty()) {
      Toast.makeText(requireContext(), "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show()
      binding.edtStudentName.requestFocus()
      return
    }

    if (phone.isEmpty()) {
      Toast.makeText(requireContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
      binding.edtStudentPhone.requestFocus()
      return
    }

    if (address.isEmpty()) {
      Toast.makeText(requireContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
      binding.edtStudentAddress.requestFocus()
      return
    }

    // Kiểm tra MSSV đã tồn tại chưa
    if (viewModel.findStudentById(id) != null) {
      Toast.makeText(requireContext(), "MSSV đã tồn tại", Toast.LENGTH_SHORT).show()
      binding.edtStudentId.requestFocus()
      return
    }

    // Thêm sinh viên mới vào ViewModel
    val newStudent = Student(id, name, phone, address)
    viewModel.addStudent(newStudent)

    Toast.makeText(requireContext(), "Đã thêm sinh viên mới", Toast.LENGTH_SHORT).show()

    // Quay lại màn hình danh sách
    findNavController().navigateUp()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
