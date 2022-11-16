package com.kok1337.feature_database_synchronization.di.module

import android.os.Environment
import com.kok1337.feature_database_synchronization.data.network.sevice.BackupService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.io.File

@Module
class DatabasePreparationProvidesModule {
    @Provides
    fun provideBackupService(retrofit: Retrofit): BackupService {
        return retrofit.create(BackupService::class.java)
    }

    @Provides
    fun provideBackupFile(): File {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return File(folder, "installer.zip")
    }
}