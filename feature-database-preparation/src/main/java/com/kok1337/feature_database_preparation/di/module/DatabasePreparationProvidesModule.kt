package com.kok1337.feature_database_preparation.di.module

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import com.kok1337.feature_database_preparation.data.network.service.FileService
import com.kok1337.feature_database_preparation.di.qualifier.*
import com.kok1337.feature_database_preparation.di.qualifier.InstallerArchive
import com.kok1337.feature_database_preparation.di.qualifier.InstallerEndpoint
import com.kok1337.feature_database_preparation.di.qualifier.TermuxPackage
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
//        return "/file/glpm_local.backup"
        return "/file/zip"
    }

    @[UserJwtToken Provides]
    fun provideUserJwtToken(): String {
        return ""
    }

    @[InstallerArchive Provides]
    fun provideInstallerArchiveFile(): File {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return File(folder, "installer.zip")
    }

    @[DirectoryToCopy Provides]
    fun provideDirectoryToCopy(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    @[ZipPassword Provides]
    fun provideZipPassword(): String {
        return "gfhjkm"
    }

    @Provides
    fun providePackageManager(context: Context): PackageManager {
        return context.packageManager
    }

    @[TermuxPackage Provides]
    fun provideTermuxPackage(): String {
        return "com.termux"
    }
}