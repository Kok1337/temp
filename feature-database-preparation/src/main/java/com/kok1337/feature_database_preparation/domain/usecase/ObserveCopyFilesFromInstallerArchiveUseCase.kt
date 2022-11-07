package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.di.qualifier.DirectoryToCopy
import com.kok1337.feature_database_preparation.di.qualifier.InstallerArchive
import com.kok1337.feature_database_preparation.di.qualifier.ZipPassword
import com.kok1337.feature_database_preparation.domain.model.CopyResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.progress.ProgressMonitor
import java.io.File
import javax.inject.Inject

class ObserveCopyFilesFromInstallerArchiveUseCase @Inject constructor(
    @DirectoryToCopy private val directoryToCopy: File,
    @InstallerArchive private val installerArchiveFile: File,
    @ZipPassword private val archivePassword: String,
) {
    suspend fun invoke() = flow {
        val zipFile = ZipFile(installerArchiveFile, archivePassword.toCharArray())

        val progressMonitor = zipFile.progressMonitor
        zipFile.isRunInThread = true
        zipFile.extractAll(directoryToCopy.absolutePath)
        while (!progressMonitor.state.equals(ProgressMonitor.State.READY)) {
            val filename = progressMonitor.fileName
            if (filename != null) {
                emit(CopyResult(filename, progressMonitor.percentDone))
            }
            delay(100)
        }
    }
}