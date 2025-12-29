package com.example.file_management

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurrentPath: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter

    private var currentDirectory: File = Environment.getExternalStorageDirectory()
    private var selectedFile: File? = null
    private var fileToCopy: File? = null

    private val permissionLauncher =
            registerForActivityResult(
                    androidx.activity.result.contract.ActivityResultContracts
                            .StartActivityForResult()
            ) {
                if (checkPermission()) {
                    loadFiles(currentDirectory)
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
        private const val MENU_RENAME = 1
        private const val MENU_DELETE = 2
        private const val MENU_COPY = 3
        private const val MENU_PASTE = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentPath = findViewById(R.id.tv_current_path)
        recyclerView = findViewById(R.id.recycler_view_files)

        adapter =
                FileAdapter(
                        onItemClick = { file -> onFileClicked(file) },
                        onItemLongClick = { file, view ->
                            selectedFile = file
                            false
                        }
                )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        onBackPressedDispatcher.addCallback(
                this,
                object : androidx.activity.OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (currentDirectory.absolutePath !=
                                        Environment.getExternalStorageDirectory().absolutePath
                        ) {
                            val parent = currentDirectory.parentFile
                            if (parent != null && parent.canRead()) {
                                loadFiles(parent)
                                return
                            }
                        }
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
        )

        if (checkPermission()) {
            loadFiles(currentDirectory)
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val read =
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            val write =
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                permissionLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                permissionLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadFiles(currentDirectory)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFiles(directory: File) {
        currentDirectory = directory
        tvCurrentPath.text = directory.absolutePath

        val files = directory.listFiles() ?: emptyArray()
        val sortedFiles =
                files.sortedWith(
                        compareBy({ !it.isDirectory }, { it.name.lowercase(Locale.getDefault()) })
                )

        adapter.submitList(sortedFiles)
    }

    private fun onFileClicked(file: File) {
        if (file.isDirectory) {
            loadFiles(file)
        } else {
            openFile(file)
        }
    }

    private fun openFile(file: File) {
        val extension = file.extension.lowercase(Locale.getDefault())
        when (extension) {
            "txt" -> showTextFile(file)
            "png", "jpg", "jpeg", "bmp" -> showImageFile(file)
            else -> Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTextFile(file: File) {
        try {
            val text = file.readText()
            AlertDialog.Builder(this)
                    .setTitle(file.name)
                    .setMessage(text)
                    .setPositiveButton("Close", null)
                    .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageFile(file: File) {
        try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val imageView = ImageView(this)
            imageView.setImageBitmap(bitmap)

            AlertDialog.Builder(this)
                    .setTitle(file.name)
                    .setView(imageView)
                    .setPositiveButton("Close", null)
                    .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val file = selectedFile ?: return

        menu.setHeaderTitle(file.name)
        menu.add(0, MENU_RENAME, 0, "Rename")
        menu.add(0, MENU_DELETE, 0, "Delete")

        if (file.isFile) {
            menu.add(0, MENU_COPY, 0, "Copy to...")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val file = selectedFile ?: return super.onContextItemSelected(item)

        when (item.itemId) {
            MENU_RENAME -> showRenameDialog(file)
            MENU_DELETE -> showDeleteDialog(file)
            MENU_COPY -> {
                fileToCopy = file
                Toast.makeText(
                                this,
                                "File copied. Navigate to destination folder and select 'Paste' from options menu.",
                                Toast.LENGTH_LONG
                        )
                        .show()
                invalidateOptionsMenu()
            }
        }
        return true
    }

    private fun showRenameDialog(file: File) {
        val input = EditText(this)
        input.setText(file.name)

        AlertDialog.Builder(this)
                .setTitle("Rename")
                .setView(input)
                .setPositiveButton("Rename") { _, _ ->
                    val newName = input.text.toString()
                    if (newName.isNotEmpty()) {
                        val newFile = File(file.parent, newName)
                        if (file.renameTo(newFile)) {
                            Toast.makeText(this, "Renamed successfully", Toast.LENGTH_SHORT).show()
                            loadFiles(currentDirectory)
                        } else {
                            Toast.makeText(this, "Rename failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun showDeleteDialog(file: File) {
        AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete ${file.name}?")
                .setPositiveButton("Delete") { _, _ ->
                    val result = if (file.isDirectory) file.deleteRecursively() else file.delete()
                    if (result) {
                        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                        loadFiles(currentDirectory)
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        if (fileToCopy != null) {
            menu.add(0, MENU_PASTE, 0, "Paste ${fileToCopy?.name} Here")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_folder -> showCreateFolderDialog()
            R.id.action_create_file -> showCreateFileDialog()
            MENU_PASTE -> {
                pasteFile()
                fileToCopy = null
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pasteFile() {
        val source = fileToCopy ?: return
        val destination = File(currentDirectory, source.name)

        try {
            source.inputStream().use { input ->
                destination.outputStream().use { output -> input.copyTo(output) }
            }
            Toast.makeText(this, "File copied successfully", Toast.LENGTH_SHORT).show()
            loadFiles(currentDirectory)
        } catch (e: Exception) {
            Toast.makeText(this, "Copy failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateFolderDialog() {
        val input = EditText(this)
        input.hint = "Folder Name"

        AlertDialog.Builder(this)
                .setTitle("New Folder")
                .setView(input)
                .setPositiveButton("Create") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotEmpty()) {
                        val newFolder = File(currentDirectory, name)
                        if (newFolder.mkdir()) {
                            Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show()
                            loadFiles(currentDirectory)
                        } else {
                            Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun showCreateFileDialog() {
        val input = EditText(this)
        input.hint = "File Name (e.g., test.txt)"

        AlertDialog.Builder(this)
                .setTitle("New Text File")
                .setView(input)
                .setPositiveButton("Create") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotEmpty()) {
                        val newFile = File(currentDirectory, name)
                        try {
                            if (newFile.createNewFile()) {
                                Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show()
                                loadFiles(currentDirectory)
                            } else {
                                Toast.makeText(
                                                this,
                                                "File already exists or failed",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
    }
}
