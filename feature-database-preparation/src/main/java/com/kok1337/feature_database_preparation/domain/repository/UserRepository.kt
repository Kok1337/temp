package com.kok1337.feature_database_preparation.domain.repository

interface UserRepository {
    suspend fun getUserId(): Int?
    suspend fun saveUserId(id: Int?)
}