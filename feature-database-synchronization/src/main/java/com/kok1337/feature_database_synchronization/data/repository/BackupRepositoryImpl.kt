package com.kok1337.feature_database_synchronization.data.repository

import com.kok1337.feature_database_synchronization.data.network.sevice.BackupService
import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import com.kok1337.network.domain.model.UploadBackupRequestBody
import com.kok1337.network.domain.model.UploadCallback
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    private val backupService: BackupService,
    private val uploadBackupEndPoint: String,
) : BackupRepository {
    override suspend fun uploadBackup(
        file: File,
        callback: UploadCallback?
    ): Response<ResponseBody> {

        val requestBody =

        val body = UploadBackupRequestBody(file, callback)
        val multipartFile = MultipartBody.Part.createFormData("backup", file.name, body)
        return backupService.uploadFile(uploadBackupEndPoint, multipartFile)
    }
}