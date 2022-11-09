package com.kok1337.feature_database_preparation.domain.usecase

import android.content.pm.PackageManager
import com.kok1337.feature_database_preparation.di.qualifier.DirectoryToCopy
import com.kok1337.feature_database_preparation.di.qualifier.InstallerArchive
import com.kok1337.feature_database_preparation.di.qualifier.TermuxPackage
import com.kok1337.feature_database_preparation.domain.model.TermuxState
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import com.kok1337.feature_database_preparation.domain.repository.UserRepository
import net.lingala.zip4j.ZipFile
import java.io.File
import javax.inject.Inject

class GetTermuxStateUseCase @Inject constructor(
    private val packageManager: PackageManager,
    @TermuxPackage private val termuxPackage: String,
    private val userRepository: UserRepository,
    @InstallerArchive private val installerArchiveFile: File,
    @DirectoryToCopy private val directory: File,
    private val fileRepository: FileRepository,
) {
    suspend fun invoke(): TermuxState {
        val isInstalled = try {
            packageManager.getApplicationInfo(termuxPackage, 0).enabled
        } catch (exception: PackageManager.NameNotFoundException) {
            false
        }
        if (!isInstalled) {
            if (!installerArchiveFile.exists()) return TermuxState.NOT_INSTALLED
            val archiveFileSize = installerArchiveFile.length()
            val expectedArchiveFileSize = fileRepository.getInstallerArchiveSize()
            if (archiveFileSize != expectedArchiveFileSize) return TermuxState.NOT_INSTALLED
            val files = ZipFile(installerArchiveFile).fileHeaders.map { it.fileName }
            val allFilesCopied = files.all { directory.list()?.contains(it) ?: false }
            if (allFilesCopied) return TermuxState.FILES_COPIED
            return TermuxState.NOT_INSTALLED
        }
        return try {
            userRepository.getUserId()
            TermuxState.WORKS_CORRECTLY
        } catch (exception: Exception) {
            exception.printStackTrace()
            TermuxState.NOT_WORKS_CORRECTLY
        }
    }
}