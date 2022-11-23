package com.kok1337.feature_database_synchronization.data.repository

import com.kok1337.feature_database_synchronization.data.network.sevice.BackupService
import com.kok1337.feature_database_synchronization.di.qualifier.DownloadBackupEndpoint
import com.kok1337.feature_database_synchronization.di.qualifier.UploadBackupEndpoint
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
    @UploadBackupEndpoint private val uploadBackupEndPoint: String,
    @DownloadBackupEndpoint private val downloadBackupEndpoint: String,
    ) : BackupRepository {
    override suspend fun uploadBackup(
        file: File,
        callback: UploadCallback?
    ): Response<ResponseBody> {
        val fileBody = UploadBackupRequestBody(file, "multipart", callback)
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, fileBody)
        return backupService.uploadBackup(uploadBackupEndPoint, multipartBody)
    }

    override suspend fun downloadBackup(): Response<ResponseBody> =
        backupService.downloadBackup(downloadBackupEndpoint)
}