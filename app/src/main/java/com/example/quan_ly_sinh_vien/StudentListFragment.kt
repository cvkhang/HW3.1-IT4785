package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    setupSearchView()
    observeViewModel()
  }

  /** Thiết lập RecyclerView */
  private fun setupRecyclerView() {
    studentAdapter =
            StudentAdapter(
                    students = emptyList(),
                    onItemClick = { student ->
                      // Navigate đến StudentDetailFragment với student object
                      val action = StudentListFragmentDirections.actionListToDetail(student)
                      findNavController().navigate(action)
                    }
            )

    binding.recyclerViewStudents.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = studentAdapter
    }
  }

  /** Thiết lập SearchView */
  private fun setupSearchView() {
    binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
              override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchStudents(query ?: "")
                return false
              }

              override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchStudents(newText ?: "")
                return true
              }
            }
    )
  }

  /** Quan sát thay đổi từ ViewModel */
  private fun observeViewModel() {
    viewModel.students.observe(viewLifecycleOwner) { students ->
      studentAdapter.updateStudents(students)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
