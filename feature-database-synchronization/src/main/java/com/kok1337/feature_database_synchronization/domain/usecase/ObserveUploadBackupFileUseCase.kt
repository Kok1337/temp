package com.kok1337.feature_database_synchronization.domain.usecase

import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import com.kok1337.network.domain.model.UploadCallback
import java.io.File
import javax.inject.Inject

class ObserveUploadBackupFileUseCase @Inject constructor(
    private val backupRepository: BackupRepository,
    @BackupFolder private val backupFolder: File,
) {
    suspend fun invoke(fileName: String, callback: UploadCallback) {
        val backupFile = File(backupFolder, fileName)
        backupRepository.uploadBackup(backupFile, callback)
    }
}