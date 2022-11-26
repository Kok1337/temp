package com.kok1337.feature_database_synchronization.domain.usecase

import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import com.kok1337.feature_database_synchronization.di.qualifier.BackupName
import com.kok1337.feature_database_synchronization.domain.exception.FileDownloadException
import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import com.kok1337.file.DownloadResult
import com.kok1337.file.saveFileWithDownloadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ObserveDownloadBackupUseCase @Inject constructor(
    @BackupFolder private val backupFolder: File,
    @BackupName private val backupName: String,
    private val backupRepository: BackupRepository,
) {
    suspend fun invoke(): Flow<DownloadResult> = flow {
        val response = backupRepository.downloadBackup()
        if (!response.isSuccessful) throw FileDownloadException(backupName)
        val responseBody = response.body() ?: throw FileDownloadException(backupName)
        val fileSize = responseBody.contentLength()
        val inputStream = responseBody.byteStream()
        val backupFile = File(backupFolder, backupName)
        saveFileWithDownloadResult(fileSize, inputStream, backupFile) { downloadResult ->
            emit(downloadResult)
        }
    }
}