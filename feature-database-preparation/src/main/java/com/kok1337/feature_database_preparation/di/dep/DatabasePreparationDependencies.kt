package com.kok1337.feature_database_preparation.di.dep

import android.content.Context
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit

interface DatabasePreparationDependencies {
    val context: Context
    val jdbcTemplate: JdbcTemplate
    val retrofit: Retrofit
    val applicationId: String
}