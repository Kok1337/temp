package com.kok1337.feature_database_preparation.di.module

import com.kok1337.feature_database_preparation.data.repository.FileRepositoryImpl
import com.kok1337.feature_database_preparation.data.repository.UserRepositoryImpl
import com.kok1337.feature_database_preparation.domain.repository.FileRepository
import com.kok1337.feature_database_preparation.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface DatabasePreparationBindsModule {
    @Binds
    @Suppress("FunctionName")
    fun bindFileRepositoryImpl_to_FileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository

    @Binds
    @Suppress("FunctionName")
    fun bindUserRepositoryImpl_to_UserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}