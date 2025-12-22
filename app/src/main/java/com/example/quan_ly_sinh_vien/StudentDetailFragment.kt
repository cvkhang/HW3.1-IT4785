package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quan_ly_sinh_vien.databinding.FragmentStudentDetailBinding

/** Fragment hiển thị chi tiết và cập nhật thông tin sinh viên Sử dụng Data Binding */
class StudentDetailFragment : Fragment() {

  private var _binding: FragmentStudentDetailBinding? = null
  private val binding
    get() = _binding!!

  // Sử dụng activityViewModels để chia sẻ ViewModel
  private val viewModel: StudentViewModel by activityViewModels()

  // Nhận arguments từ Navigation
  private val args: StudentDetailFragmentArgs by navArgs()

  private var currentStudent: Student? = null

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    // Inflate layout với Data Binding
    _binding = FragmentStudentDetailBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    loadStudentData()
    setupButtons()
  }

  /** Load dữ liệu sinh viên từ ViewModel */
  private fun loadStudentData() {
    currentStudent = viewModel.findStudentById(args.studentId)

    if (currentStudent == null) {
      Toast.makeText(requireContext(), "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show()
      findNavController().navigateUp()
      return
    }

    // Set dữ liệu cho Data Binding
    binding.student = currentStudent
  }

  /** Thiết lập các button */
  private fun setupButtons() {
    binding.btnUpdate.setOnClickListener { updateStudent() }

    binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
  }

  /** Cập nhật thông tin sinh viên */
  private fun updateStudent() {
    val name = binding.edtStudentName.text.toString().trim()
    val phone = binding.edtStudentPhone.text.toString().trim()
    val address = binding.edtStudentAddress.text.toString().trim()

    // Validate dữ liệu
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

    // Cập nhật thông tin trong ViewModel
    currentStudent?.let { student ->
      viewModel.updateStudent(student.id, name, phone, address)
      Toast.makeText(requireContext(), "Đã cập nhật thông tin", Toast.LENGTH_SHORT).show()
      findNavController().navigateUp()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
