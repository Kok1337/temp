package com.kok1337.feature_database_synchronization.domain.repository

import com.kok1337.network.domain.model.UploadCallback
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface BackupRepository {
    suspend fun uploadBackup(file: File, callback: UploadCallback?): Response<ResponseBody>
}