package com.kok1337.file

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class DownloadResult(private val savedSize: Long, private val fileSize: Long) {
    private fun convertFileSizeToString(fileSize: Long): String {
        if (fileSize <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(fileSize.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(fileSize / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    val savedProgress get() = (savedSize * 100 / fileSize).toInt()

    override fun toString(): String {
        val convertedSavedSize = convertFileSizeToString(savedSize)
        val convertedFileSize = convertFileSizeToString(fileSize)
        return "$convertedSavedSize / $convertedFileSize"
    }
}

typealias OnBlockSaved = suspend (DownloadResult) -> Unit

suspend fun saveFileWithDownloadResult(
    fileSize: Long,
    iStream: InputStream,
    file: File,
    onBlockSaved: OnBlockSaved
) {
    iStream.use {
        file.parentFile?.mkdirs()
        FileOutputStream(file).use { oStream ->
            val buffer = ByteArray(8 * 1024)
            var len: Int
            var total: Long = 0
            while (iStream.read(buffer).also { len = it } != -1) {
                total += len.toLong()
                onBlockSaved.invoke(DownloadResult(total, fileSize))
                oStream.write(buffer, 0, len)
            }
        }
    }
}