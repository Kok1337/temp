package com.kok1337.feature_database_synchronization.di.module

import com.kok1337.feature_database_synchronization.data.repository.BackupRepositoryImpl
import com.kok1337.feature_database_synchronization.domain.repository.BackupRepository
import dagger.Binds
import dagger.Module

@Module
interface DatabasePreparationBindsModule {
    @Binds
    @Suppress("FunctionName")
    fun BackupRepositoryImpl_to_BackupRepository(backupRepositoryImpl: BackupRepositoryImpl): BackupRepository
}