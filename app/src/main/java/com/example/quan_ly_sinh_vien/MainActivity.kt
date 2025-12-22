package com.example.quan_ly_sinh_vien

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

/** MainActivity chứa NavHostFragment Sử dụng Navigation Component để quản lý các Fragment */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Thiết lập Navigation
        setupNavigation()
    }

    /** Thiết lập Navigation Component */
    private fun setupNavigation() {
        // Lấy NavHostFragment
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        // Thiết lập ActionBar với NavController
        try {
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
        } catch (e: Exception) {
            // Nếu lỗi ActionBar, bỏ qua và chạy tiếp
            e.printStackTrace()
        }
    }

    /** Xử lý nút back trên ActionBar */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
