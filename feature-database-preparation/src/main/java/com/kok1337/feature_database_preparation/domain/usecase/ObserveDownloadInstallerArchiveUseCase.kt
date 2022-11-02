package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.domain.exception.FileDownloadException
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import com.kok1337.file.DownloadResult
import com.kok1337.file.saveFileWithDownloadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ObserveDownloadInstallerArchiveUseCase @Inject constructor(
    private val installerArchiveFile: File,
    private val fileRepository: FileRepository,
) {
    suspend fun invoke(): Flow<DownloadResult> = flow {
        val response = fileRepository.downloadInstallerArchive()
        if (!response.isSuccessful) throw FileDownloadException(installerArchiveFile.name)
        val responseBody = response.body() ?: throw FileDownloadException(installerArchiveFile.name)
        val fileSize = responseBody.contentLength()
        val inputStream = responseBody.byteStream()
        saveFileWithDownloadResult(fileSize, inputStream, installerArchiveFile) { downloadResult ->
            emit(downloadResult)
        }
    }
}