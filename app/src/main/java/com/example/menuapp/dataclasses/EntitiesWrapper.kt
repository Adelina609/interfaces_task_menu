package com.example.menuapp.dataclasses

class EntitiesWrapper {
    var file: FileEntity? = null
    var dir: PackageEntity? = null

    fun isFile(): Boolean {
        return file != null
    }
}