package com.kok1337.feature_database_synchronization.di.module

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.kok1337.feature_database_synchronization.data.network.sevice.BackupService
import com.kok1337.feature_database_synchronization.di.qualifier.*
import com.kok1337.feature_database_synchronization.di.qualifier.BackupFolder
import com.kok1337.feature_database_synchronization.di.qualifier.BackupName
import com.kok1337.feature_database_synchronization.di.qualifier.CreateBackupScript
import com.kok1337.feature_database_synchronization.di.qualifier.DownloadBackupEndpoint
import com.kok1337.feature_database_synchronization.di.qualifier.RestoreBackupScript
import com.kok1337.feature_database_synchronization.di.qualifier.UploadBackupEndpoint
import com.kok1337.feature_database_synchronization.domain.factory.BackupNameFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Module
class DatabaseSynchronizationProvidesModule {
    @Provides
    fun provideBackupService(retrofit: Retrofit): BackupService {
        return retrofit.create(BackupService::class.java)
    }

    @SuppressLint("SdCardPath")
    @[CreateBackupScript Provides]
    fun provideCreateBackupScriptPath(): String {
        return "/data/data/com.termux/files/usr/bin/dump_changes"
    }

    @SuppressLint("SdCardPath")
    @[RestoreBackupScript Provides]
    fun provideRestoreBackupScriptPath(): String {
        return "/data/data/com.termux/files/usr/bin/restore_changes"
    }

    @[BackupFolder Provides]
    fun provideBackupFolder(): File {
        val downloadDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val backupDirectory = File(downloadDirectory, "backup")
        if (!backupDirectory.exists()) backupDirectory.mkdirs()
        return backupDirectory
    }

    @[UploadBackupEndpoint Provides]
    fun provideUploadBackupEndpoint(): String {
        return "/file/upload"
    }

    @[DownloadBackupEndpoint Provides]
    fun provideDownloadBackupEndpoint(): String {
        return "/file/download"
    }

    @[BackupName Provides]
    fun provideBackupName(): String {
        return "restore.backup"
    }

    @Provides
    fun provideBackupNameFactory(): BackupNameFactory {
        return object : BackupNameFactory {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun create(): String {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                val dateTimeString = LocalDateTime.now().format(dateTimeFormatter)
                return "$dateTimeString.backup"
            }
        }
    }
}