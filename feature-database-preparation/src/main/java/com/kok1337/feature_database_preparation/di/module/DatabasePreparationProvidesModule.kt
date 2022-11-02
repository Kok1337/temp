package com.kok1337.feature_database_preparation.di.module

import android.os.Environment
import com.kok1337.feature_database_preparation.data.service.FileService
import com.kok1337.feature_database_preparation.di.qualifier.InstallerEndpoint
import com.kok1337.feature_database_preparation.di.qualifier.UserJwtToken
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.io.File

@Module
class DatabasePreparationProvidesModule {
    @Provides
    fun provideFileService(retrofit: Retrofit): FileService {
        return retrofit.create(FileService::class.java)
    }

    @[InstallerEndpoint Provides]
    fun provideInstallerEndpoint(): String {
        return "/file/glpm_local.backup"
    }

    @[UserJwtToken Provides]
    fun provideUserJwtToken(): String {
        return ""
    }

    @Provides
    fun provideInstallerArchiveFile(): File {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return File(folder, "glpm_local.backup")
    }
}