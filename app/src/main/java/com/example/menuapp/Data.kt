package com.example.menuapp

import com.example.menuapp.dataclasses.FileEntity
import com.example.menuapp.dataclasses.PackageEntity

class Data {
    companion object {
        var packages = mutableListOf<PackageEntity>()

        fun setFileDeleted(file: FileEntity) {
            packages[file.packagePosition].list.find { it.name == file.name }.apply {
                this?.isDeleted = true
            }
        }

        fun setDirDeleted(position: Int) {
            packages[position].isDeleted = true
        }

        fun setDirPartDeleted(position: Int) {
            val list = packages[position].list
            list.forEachIndexed { index, fileEntity ->
                if(index != list.size-1) {
                    fileEntity.isDeleted = true
                }
            }
        }

        fun getFiles(position: Int): MutableList<FileEntity> {
            val files =  mutableListOf<FileEntity>()
            for (i in 0..7) {
                files.add(FileEntity(name = "$i image.jpg", isDeleted = false, position))
            }
            return files
        }

        fun canDeleteFile(position: Int, packagePosition: Int): Boolean {
            return !(packagePosition == packages.size - 1 &&
                    position == packages[packagePosition].list.filter{ f -> !f.isDeleted}.size - 1)
        }
    }

    init {
        packages = mutableListOf()
        for (i in 0..3) {
            packages.add(PackageEntity(name = "$i directory", isDeleted = false, list = getFiles(i)))
        }
    }
}