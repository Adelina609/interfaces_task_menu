package com.example.menuapp.dataclasses

data class PackageEntity(
    val name: String,
    var isDeleted: Boolean,
    var list: MutableList<FileEntity>
)