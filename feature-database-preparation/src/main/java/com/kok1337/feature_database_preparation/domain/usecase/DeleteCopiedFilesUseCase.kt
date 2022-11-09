package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.di.qualifier.DirectoryToCopy
import com.kok1337.feature_database_preparation.di.qualifier.InstallerArchive
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import net.lingala.zip4j.ZipFile
import java.io.File
import javax.inject.Inject

class DeleteCopiedFilesUseCase @Inject constructor(
    @InstallerArchive private val installerArchiveFile: File,
    @DirectoryToCopy private val directory: File,
    private val fileRepository: FileRepository,
) {
    suspend fun invoke() {
        if (!installerArchiveFile.exists()) return
        val archiveFileSize = installerArchiveFile.length()
        val expectedArchiveFileSize = fileRepository.getInstallerArchiveSize()
        if (archiveFileSize != expectedArchiveFileSize) return
        ZipFile(installerArchiveFile).fileHeaders.map { it.fileName }.forEach { fileName ->
            val file = File(directory, fileName)
            if (file.exists()) file.delete()
        }
    }
}