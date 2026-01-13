package com.example.quan_ly_sinh_vien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quan_ly_sinh_vien.databinding.FragmentStudentDetailBinding

/** Fragment hiển thị chi tiết và cập nhật thông tin sinh viên Sử dụng Data Binding */
class StudentDetailFragment : Fragment() {

  private var _binding: FragmentStudentDetailBinding? = null
  private val binding
    get() = _binding!!

  // Nhận arguments từ Navigation (passed as Parcelable)
  private val args: StudentDetailFragmentArgs by navArgs()

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

    binding.student = args.student

    // Load image using Glide manually if BindingAdapter not working, or define BindingAdapter
    com.bumptech.glide.Glide.with(this)
            .load(args.student.fullThumbnailUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.ivDetailThumbnail)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
