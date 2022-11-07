package com.kok1337.feature_database_preparation.domain.model

data class CopyResult(private val file: String, val percent: Int) {
    companion object {
        val default = CopyResult("", 0)
    }
    val fileName = file.substring(file.lastIndexOf("/")+1)
}