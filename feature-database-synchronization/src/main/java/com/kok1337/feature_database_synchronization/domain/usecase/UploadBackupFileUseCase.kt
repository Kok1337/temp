package com.kok1337.feature_database_synchronization.domain.usecase

import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import com.kok1337.file.DownloadResult
import com.kok1337.network.domain.model.UploadCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UploadBackupFileUseCase @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupFile: File,
) {
    suspend fun invoke(): Flow<DownloadResult> = flow {
        val uploadCallback: UploadCallback =
            { savedSize: Long, fileSize: Long -> emit(DownloadResult(savedSize, fileSize)) }
        backupRepository.uploadBackup(backupFile, uploadCallback)
    }
}