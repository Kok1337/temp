package com.kok1337.feature_database_preparation.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit
import javax.inject.Inject

internal class DatabasePreparationViewModel (
    private val retrofit: Retrofit,
    private val jdbcTemplate: JdbcTemplate,
): ViewModel() {

//    private val _termuxState = MutableStateFlow<Result<>>()

    fun print() {
        Log.e(javaClass.simpleName, jdbcTemplate.toString())
        Log.e(javaClass.simpleName, retrofit.toString())
    }

    class Factory @Inject constructor(
        private val retrofit: Retrofit,
        private val jdbcTemplate: JdbcTemplate,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabasePreparationViewModel::class.java)
            return DatabasePreparationViewModel(
                retrofit = retrofit,
                jdbcTemplate = jdbcTemplate
            ) as T
        }
    }
}