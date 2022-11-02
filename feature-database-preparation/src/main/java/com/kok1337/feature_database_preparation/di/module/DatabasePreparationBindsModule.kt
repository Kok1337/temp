package com.kok1337.feature_database_preparation.di.module

import com.kok1337.feature_database_preparation.data.repository.FileRepositoryImpl
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import dagger.Binds
import dagger.Module

@Module
interface DatabasePreparationBindsModule {
    @Binds
    @Suppress("FunctionName")
    fun bindFileRepositoryImpl_to_FileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository
}