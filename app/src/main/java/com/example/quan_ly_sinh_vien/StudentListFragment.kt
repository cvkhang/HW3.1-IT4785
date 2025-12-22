package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quan_ly_sinh_vien.databinding.FragmentStudentListBinding

/** Fragment hiển thị danh sách sinh viên */
class StudentListFragment : Fragment() {

  private var _binding: FragmentStudentListBinding? = null
  private val binding
    get() = _binding!!

  // Sử dụng activityViewModels để chia sẻ ViewModel với các Fragment khác
  private val viewModel: StudentViewModel by activityViewModels()

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    _binding = FragmentStudentListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupRecyclerView()
    setupFabButton()
    observeViewModel()
  }

  /** Thiết lập RecyclerView */
  private fun setupRecyclerView() {
    studentAdapter =
            StudentAdapter(
                    students = viewModel.getStudents(),
                    onItemClick = { student ->
                      // Navigate đến StudentDetailFragment với studentId
                      val action = StudentListFragmentDirections.actionListToDetail(student.id)
                      findNavController().navigate(action)
                    },
                    onDeleteClick = { student -> showDeleteConfirmation(student) }
            )

    binding.recyclerViewStudents.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = studentAdapter
    }
  }

  /** Thiết lập FAB button */
  private fun setupFabButton() {
    binding.fabAddStudent.setOnClickListener {
      // Navigate đến AddStudentFragment
      findNavController().navigate(R.id.action_list_to_add)
    }
  }

  /** Quan sát thay đổi từ ViewModel */
  private fun observeViewModel() {
    viewModel.students.observe(viewLifecycleOwner) { students ->
      studentAdapter.updateStudents(students)
    }
  }

  /** Hiển thị dialog xác nhận xóa */
  private fun showDeleteConfirmation(student: Student) {
    AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.name}?")
            .setPositiveButton("Xóa") { _, _ -> viewModel.removeStudent(student) }
            .setNegativeButton("Hủy", null)
            .show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
