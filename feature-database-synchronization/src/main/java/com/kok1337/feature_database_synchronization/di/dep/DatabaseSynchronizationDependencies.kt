package com.kok1337.feature_database_synchronization.di.dep

import android.content.Context
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit

interface DatabaseSynchronizationDependencies {
    val context: Context
    val jdbcTemplate: JdbcTemplate
    val retrofit: Retrofit
}