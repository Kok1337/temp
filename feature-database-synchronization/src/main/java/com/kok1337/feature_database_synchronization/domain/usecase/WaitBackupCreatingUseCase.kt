package com.kok1337.feature_database_synchronization.domain.usecase

import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

class WaitBackupCreatingUseCase @Inject constructor(
    @BackupFolder private val backupDirectory: File,
) {
    suspend fun invoke(fileName: String) {
        while (true) {
            val backupFile = File(backupDirectory, fileName)
            if (backupFile.exists() && backupFile.isFile) return
            delay(100)
        }
    }
}