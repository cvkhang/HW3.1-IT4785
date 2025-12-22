# 📱 Quản Lý Sinh Viên - Android App

Ứng dụng quản lý sinh viên sử dụng kiến trúc hiện đại với **Fragment**, **Navigation Component**, **ViewModel** và **Data Binding**.

## 🎯 Mô tả

Ứng dụng Android giúp quản lý danh sách sinh viên với các tính năng CRUD (Create, Read, Update, Delete) đầy đủ. Được xây dựng theo kiến trúc MVVM pattern với các công nghệ hiện đại nhất của Android.

## ✨ Tính năng

### 📋 Quản lý sinh viên
- ✅ **Xem danh sách sinh viên** - Hiển thị MSSV và Họ tên
- ✅ **Thêm sinh viên mới** - Form nhập đầy đủ thông tin với validation
- ✅ **Xem chi tiết sinh viên** - Hiển thị tất cả thông tin
- ✅ **Cập nhật thông tin** - Chỉnh sửa Họ tên, SĐT, Địa chỉ
- ✅ **Xóa sinh viên** - Với dialog xác nhận

### 🎨 Giao diện
- Material Design 3
- Smooth animations & transitions
- Responsive layouts
- FAB button cho thêm nhanh
- Color-coded buttons (Đỏ = Hủy, Tím = Xác nhận)

## 🏗️ Kiến trúc & Công nghệ

### Kiến trúc
- **MVVM Pattern** (Model-View-ViewModel)
- **Single Activity Architecture**
- **Repository Pattern** (via ViewModel)

### Công nghệ sử dụng
| Công nghệ | Mục đích |
|-----------|----------|
| **Kotlin** | Ngôn ngữ lập trình chính |
| **Fragment** | UI Components |
| **Navigation Component** | Điều hướng giữa các màn hình |
| **ViewModel** | Quản lý state & business logic |
| **LiveData** | Observable data holder |
| **Data Binding** | Bind UI với data |
| **RecyclerView** | Hiển thị danh sách |
| **Material Components** | UI/UX components |

## 📁 Cấu trúc Project

```
app/src/main/
├── java/com/example/quan_ly_sinh_vien/
│   ├── MainActivity.kt                 # Activity chính chứa NavHostFragment
│   ├── StudentViewModel.kt             # ViewModel quản lý dữ liệu
│   ├── StudentListFragment.kt          # Fragment danh sách
│   ├── AddStudentFragment.kt           # Fragment thêm mới
│   ├── StudentDetailFragment.kt        # Fragment chi tiết
│   ├── Student.kt                      # Data class
│   └── StudentAdapter.kt               # RecyclerView Adapter
│
├── res/
│   ├── layout/
│   │   ├── activity_main.xml           # Layout chứa NavHostFragment
│   │   ├── fragment_student_list.xml   # Layout danh sách
│   │   ├── fragment_add_student.xml    # Layout thêm (Data Binding)
│   │   ├── fragment_student_detail.xml # Layout chi tiết (Data Binding)
│   │   └── item_student.xml            # Layout item RecyclerView
│   │
│   └── navigation/
│       └── nav_graph.xml               # Navigation Graph
```

## 🔄 Luồng điều hướng

```
StudentListFragment (Danh sách)
    ├─► AddStudentFragment (Thêm mới)
    └─► StudentDetailFragment (Chi tiết/Cập nhật)
```

## 🚀 Cài đặt & Chạy

### Yêu cầu hệ thống
- Android Studio Hedgehog (2023.1.1) hoặc mới hơn
- Android SDK 24 (Android 7.0) trở lên
- Kotlin 2.0.21
- Gradle 8.7.3

### Các bước cài đặt

1. **Clone repository**
```bash
git clone https://github.com/cvkhang/quanlysinhvien.git
cd quanlysinhvien
```

2. **Mở project trong Android Studio**
```
File → Open → Chọn thư mục project
```

3. **Sync Gradle**
```
File → Sync Project with Gradle Files
```

4. **Build Project**
```
Build → Rebuild Project
```

5. **Chạy app**
```
Run → Run 'app'
```

## 📦 Dependencies

```kotlin
dependencies {
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    
    // Material Design
    implementation("com.google.android.material:material:1.12.0")
    
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    
    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
}
```

## 🎯 Các điểm nổi bật

### So sánh với kiến trúc cũ

| Cũ (Activity-based) | Mới (Fragment-based) |
|---------------------|---------------------|
| ❌ Multiple Activities | ✅ Single Activity |
| ❌ Intent navigation | ✅ Navigation Component |
| ❌ Static data manager | ✅ ViewModel + LiveData |
| ❌ findViewById | ✅ Data Binding |
| ❌ Manual navigation | ✅ Safe Args |
| ❌ Option Menu | ✅ FAB Button |

### Data Binding
- Two-way binding với form input
- Automatic UI updates
- Type-safe binding

### Navigation Component
- Type-safe arguments với Safe Args
- Automatic back stack management
- Smooth transitions

### ViewModel
- Survives configuration changes
- Shared between Fragments
- Lifecycle-aware

## 📝 Thông tin sinh viên

Mỗi sinh viên bao gồm:
- **MSSV** (Mã số sinh viên) - Không thể sửa sau khi tạo
- **Họ tên** - Có thể cập nhật
- **Số điện thoại** - Có thể cập nhật
- **Địa chỉ** - Có thể cập nhật

## 🤝 Đóng góp

Mọi đóng góp đều được chào đón! Hãy tạo Pull Request hoặc Issue nếu bạn có ý tưởng cải thiện.

## 📄 License

Dự án này được phát hành dưới giấy phép MIT License.

## 👨‍💻 Tác giả

**Bài tập IT4785 - Phát triển ứng dụng di động**

---

⭐ **Nếu thấy project hữu ích, hãy cho một star nhé!** ⭐

