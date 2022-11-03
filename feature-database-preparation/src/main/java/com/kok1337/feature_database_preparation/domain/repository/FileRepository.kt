package com.kok1337.feature_database_preparation.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface FileRepository {
    suspend fun getInstallerArchiveSize(): Long
    suspend fun saveInstallerArchiveSize(installerArchiveSize: Long)
    suspend fun downloadInstallerArchive(): Response<ResponseBody>
}