package com.kok1337.feature_database_synchronization.data.network.sevice

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BackupService {
    @[POST Multipart]
    suspend fun uploadBackup(
        @Url uploadBackupEndPoint: String,
        @Part file: MultipartBody.Part,
    ): Response<ResponseBody>

    @[GET Streaming]
    suspend fun downloadBackup(
        @Url installerArchiveEndpoint: String,
    ): Response<ResponseBody>
}