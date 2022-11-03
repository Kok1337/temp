package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.domain.model.InstallerState
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

class GetInstallerState @Inject constructor(
    private val archiveFile: File,
    private val fileRepository: FileRepository,
) {
    suspend fun invoke(): InstallerState {
        if (!archiveFile.exists()) return InstallerState.NOT_DOWNLOADED
        val archiveFileSize = archiveFile.length()
        val expectedArchiveFileSize = fileRepository.getInstallerArchiveSize()
        if (archiveFileSize != expectedArchiveFileSize) return InstallerState.NOT_DOWNLOADED
        return InstallerState.DOWNLOADED
    }
}