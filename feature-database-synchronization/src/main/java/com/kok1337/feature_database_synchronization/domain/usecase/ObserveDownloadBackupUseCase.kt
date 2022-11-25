package com.kok1337.feature_database_synchronization.domain.usecase

import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import com.kok1337.feature_database_synchronization.domain.exception.FileDownloadException
import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import com.kok1337.file.DownloadResult
import com.kok1337.file.saveFileWithDownloadResult
import kotlinx.coroutines.flow.flow
import java.io.File
import java.util.concurrent.Flow
import javax.inject.Inject

class ObserveDownloadBackupUseCase @Inject constructor(
    @BackupFolder private val backupFolder: File,
//    private val backupName: String,
    private val backupRepository: BackupRepository,
) {
//    suspend fun invoke(): Flow<DownloadResult> = flow {
//        val response = backupRepository.downloadBackup()
//        if (!response.isSuccessful) throw FileDownloadException(installerArchiveFile.name)
//        val responseBody = response.body() ?: throw FileDownloadException(installerArchiveFile.name)
//        val fileSize = responseBody.contentLength()
//        val inputStream = responseBody.byteStream()
//        fileRepository.saveInstallerArchiveSize(fileSize)
//        saveFileWithDownloadResult(fileSize, inputStream, installerArchiveFile) { downloadResult ->
//            emit(downloadResult)
//        }
//    }
}