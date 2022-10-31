package com.kok1337.feature_database_preparation.data.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileService {
    @[GET Streaming]
    suspend fun downloadInstallerArchive(@Url installerArchiveEndpoint: String): Response<ResponseBody>
}