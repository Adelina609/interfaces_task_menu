package com.example.menuapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.dataclasses.EntitiesWrapper
import com.example.menuapp.dataclasses.FileEntity
import com.example.menuapp.dataclasses.PackageEntity
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenu
import com.github.zawadz88.materialpopupmenu.popupMenu
import kotlinx.android.synthetic.main.item_dir.view.*
import kotlinx.android.synthetic.main.item_file.view.*
import java.util.*

class FileDirAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: ArrayList<EntitiesWrapper> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var deleteFile: (FileEntity) -> Unit
    lateinit var deleteDir: (Int, Boolean) -> Unit
    lateinit var openFiles: (List<FileEntity>) -> Unit
    lateinit var showErrorByDeletingFile: () -> Unit

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isFile()) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (p1) {
            0 -> FilesViewHolder(inflater.inflate(R.layout.item_file, parent, false))
            1 -> DirViewHolder(inflater.inflate(R.layout.item_dir, parent, false))
            else -> throw IllegalArgumentException("Incorrect view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        when (viewholder) {
            is FilesViewHolder -> list[position].file?.let { viewholder.bind(it, position) }
            is DirViewHolder -> list[position].dir?.let { viewholder.bind(it, position) }
        }
    }

    private fun deleteFile(file: FileEntity, position: Int) {
        if (Data.canDeleteFile(position, file.packagePosition)) {
            deleteFile.invoke(file)
            list.removeAt(position)
            notifyDataSetChanged()
        } else showErrorByDeletingFile()
    }

    private fun deleteDir(position: Int) {
        deleteDir.invoke(position, position == list.size - 1)
        if (position != list.size - 1) {
            list.removeAt(position)
            notifyDataSetChanged()
        }
    }

    inner class FilesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(file: FileEntity, position: Int) {
            itemView.tv_doc_name.text = file.name
            itemView.iv_dots.setOnClickListener {
                getFilesPopup(file, position).show(itemView.context, it)
            }
        }
    }

    inner class DirViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(dir: PackageEntity, position: Int) {
            itemView.tv_doc_name_dir.text = dir.name
            itemView.iv_dots_dir.setOnClickListener {
                getDirPopup(position).show(itemView.context, it)
            }
            itemView.setOnClickListener {
                openFiles(dir.list)
            }
        }
    }

    private fun getFilesPopup(file: FileEntity, position: Int): MaterialPopupMenu {
        return popupMenu {
            section {
                title = "Меню"
                item {
                    label = "Удалить"
                    labelColor = Color.BLACK
                    callback = {
                        deleteFile(file, position)
                    }
                }
            }
        }
    }

    private fun getDirPopup(position: Int): MaterialPopupMenu {
        return popupMenu {
            section {
                title = "Меню"
                item {
                    label = "Удалить"
                    labelColor = Color.BLACK
                    callback = {
                        deleteDir(position)
                    }
                }
            }
        }
    }
}