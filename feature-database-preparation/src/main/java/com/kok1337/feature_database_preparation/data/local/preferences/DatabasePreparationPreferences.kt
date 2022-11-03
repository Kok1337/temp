package com.kok1337.feature_database_preparation.data.local.preferences

import android.content.Context
import javax.inject.Inject

class DatabasePreparationPreferences @Inject constructor(
    context: Context,
) {
    companion object {
        private const val SHARED_PREFERENCES_NAME = "DatabasePreparationPreferences"
        private const val INSTALLER_ARCHIVE_SIZE = "InstallerArchiveSize"
        private const val DEFAULT_INSTALLER_ARCHIVE_SIZE = 0L
    }

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getInstallerArchiveSize(): Long =
        sharedPreferences.getLong(INSTALLER_ARCHIVE_SIZE, DEFAULT_INSTALLER_ARCHIVE_SIZE)

    fun saveInstallerArchiveSize(size: Long) =
        sharedPreferences.edit().putLong(INSTALLER_ARCHIVE_SIZE, size).apply()
}