package com.kok1337.feature_database_preparation.data.repository

import com.kok1337.feature_database_preparation.data.service.FileService
import com.kok1337.feature_database_preparation.di.qualifier.InstallerEndpoint
import com.kok1337.feature_database_preparation.di.qualifier.UserJwtToken
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileService: FileService,
    @UserJwtToken private val userJwtToken: String,
    @InstallerEndpoint private val installerEndpoint: String,
) : FileRepository {
    override suspend fun downloadInstallerArchive(): Response<ResponseBody> =
        fileService.downloadInstallerArchive(installerEndpoint)
}