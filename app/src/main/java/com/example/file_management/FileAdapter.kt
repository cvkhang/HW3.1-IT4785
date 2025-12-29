package com.example.file_management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(
    private val onItemClick: (File) -> Unit,
    private val onItemLongClick: (File, View) -> Boolean
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private var files: List<File> = emptyList()

    fun submitList(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int = files.size

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_file_icon)
        private val tvName: TextView = itemView.findViewById(R.id.tv_file_name)

        fun bind(file: File) {
            tvName.text = file.name
            
            if (file.isDirectory) {
                ivIcon.setImageResource(android.R.drawable.ic_menu_view) // Folder icon placeholder
            } else {
                ivIcon.setImageResource(android.R.drawable.ic_menu_agenda) // File icon placeholder
                
                // Simple extension check for images to show a different icon potentially
                val extension = file.extension.lowercase()
                if (extension in listOf("jpg", "jpeg", "png", "bmp")) {
                    ivIcon.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }

            itemView.setOnClickListener { onItemClick(file) }
            itemView.setOnLongClickListener { onItemLongClick(file, itemView) }
        }
    }
}
