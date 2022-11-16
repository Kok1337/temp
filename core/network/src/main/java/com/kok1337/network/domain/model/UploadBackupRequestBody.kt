package com.kok1337.network.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadBackupRequestBody(
    private val file: File,
    private val flow: MutableStateFlow<UploadProgress>,
) : RequestBody() {
    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    override fun contentLength(): Long = file.length()

    override fun contentType() = "multipart/form-data".toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        val fileSize = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                flow.value = UploadProgress(uploaded, fileSize)
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }
}
