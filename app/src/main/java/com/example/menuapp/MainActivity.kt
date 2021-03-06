package com.example.menuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.menuapp.dataclasses.EntitiesWrapper
import com.example.menuapp.dataclasses.FileEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FileDirAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Data()
        setAdapter()
        openPackages()
    }

    private fun setAdapter() {
        adapter = FileDirAdapter()
        adapter.openFiles = {
            openFiles(it.filter { f -> !f.isDeleted }.toMutableList())
        }
        adapter.deleteFile = {
            deleteFile(it)
        }
        adapter.deleteDir = { position, shouldDelete ->
            if (shouldDelete) deletePartOfDirectory(position)
            else deleteDirectory(position)
        }
        adapter.showErrorByDeletingFile = {
            showErrorByDeletingFile()
        }
        rv_files_dirs.adapter = adapter
        rv_files_dirs.layoutManager = LinearLayoutManager(this)
        rv_files_dirs.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun openPackages() {
        tv_back.visibility = View.GONE
        val list = mutableListOf<EntitiesWrapper>()
        Data.packages.forEach {
            if (!it.isDeleted) list.add(EntitiesWrapper().apply { dir = it })
        }
        adapter.list = list as ArrayList<EntitiesWrapper>
    }

    private fun openFiles(files: MutableList<FileEntity>) {
        tv_back.visibility = View.VISIBLE
        tv_back.setOnClickListener {
            openPackages()
        }
        val list = mutableListOf<EntitiesWrapper>()
        files.forEach {
            if (!it.isDeleted) list.add(EntitiesWrapper().apply { file = it })
        }
        adapter.list = list as ArrayList<EntitiesWrapper>
    }

    private fun deleteFile(file: FileEntity) {
        Data.setFileDeleted(file)
        Toast.makeText(this,"???????? ???????????? ??????????????", Toast.LENGTH_SHORT).show()
    }

    private fun deleteDirectory(position: Int) {
        Data.setDirDeleted(position)
        Toast.makeText(this,"?????????? ?????????????? ??????????????", Toast.LENGTH_SHORT).show()
    }

    private fun deletePartOfDirectory(position: Int) {
        Data.setDirPartDeleted(position)
        Toast.makeText(this,"???? ?????????????? ?????????????? ?????????? - ???????? '7 image.jpg' ????????????????????.", Toast.LENGTH_SHORT).show()
    }

    private fun showErrorByDeletingFile() {
        Toast.makeText(this,"???? ?????????????? ?????????????? ???????? '7 image.jpg'.", Toast.LENGTH_SHORT).show()
    }
}