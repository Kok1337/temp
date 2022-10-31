package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.domain.exception.FileDownloadException
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import com.kok1337.file.DownloadResult
import com.kok1337.file.saveFileWithDownloadResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.Result
import com.kok1337.result.SuccessResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class DownloadInstallerArchiveUseCase @Inject constructor(
    private val installerArchiveFile: File,
    private val fileRepository: FileRepository,
) {
    fun invoke(): Flow<Result<DownloadResult>> = flow {
        val response = fileRepository.downloadInstallerArchive()
        if (!response.isSuccessful) {
            emit(ErrorResult(FileDownloadException(installerArchiveFile.name)))
            return@flow
        }

        val responseBody = response.body()
        if (responseBody == null) {
            emit(ErrorResult(FileDownloadException(installerArchiveFile.name)))
            return@flow
        }

        val fileSize = responseBody.contentLength()
        val inputStream = responseBody.byteStream()
        saveFileWithDownloadResult(fileSize, inputStream, installerArchiveFile) { downloadResult ->
            emit(SuccessResult(downloadResult))
        }
    }
}