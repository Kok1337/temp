package com.kok1337.network.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadBackupRequestBody(
    private val file: File,
    private val contentType: String,
    private val callback: UploadCallback?,
) : RequestBody() {
    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    override fun contentLength(): Long = file.length()

    override fun contentType() = "$contentType/*".toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        val fileSize = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                callback?.invoke(uploaded, fileSize)
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
        callback?.invoke(uploaded, fileSize)
    }
}
