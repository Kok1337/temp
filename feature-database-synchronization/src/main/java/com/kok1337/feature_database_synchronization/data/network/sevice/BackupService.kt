package com.kok1337.feature_database_synchronization.data.network.sevice

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface BackupService {
    @[POST Multipart]
//    @Headers("Content-Type: multipart/*")
    suspend fun uploadFile(
        @Url uploadBackupEndPoint: String,
        @Part file: MultipartBody.Part,
//        @Part("desc") desc: RequestBody
    ): Response<ResponseBody>
}