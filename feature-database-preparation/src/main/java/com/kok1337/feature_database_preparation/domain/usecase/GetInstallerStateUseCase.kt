package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.di.qualifier.InstallerArchive
import com.kok1337.feature_database_preparation.domain.model.InstallerState
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

class GetInstallerStateUseCase @Inject constructor(
    @InstallerArchive private val installerArchiveFile: File,
    private val installerIsRequiredUseCase: InstallerIsRequiredUseCase,
    private val fileRepository: FileRepository,
) {
    suspend fun invoke(): InstallerState {
        val isRequired = installerIsRequiredUseCase.invoke()
        val incorrectState = if (!isRequired) InstallerState.NOT_REQUIRED
        else InstallerState.NOT_DOWNLOADED
        if (!installerArchiveFile.exists()) return incorrectState
        val archiveFileSize = installerArchiveFile.length()
        val expectedArchiveFileSize = fileRepository.getInstallerArchiveSize()
        if (archiveFileSize != expectedArchiveFileSize) return incorrectState
        return InstallerState.DOWNLOADED
    }
}