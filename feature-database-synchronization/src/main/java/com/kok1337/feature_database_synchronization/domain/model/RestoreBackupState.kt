package com.kok1337.feature_database_synchronization.domain.model

enum class RestoreBackupState {
    DOWNLOAD_STARTED,
    DOWNLOADED,
    DOWNLOAD_ERROR,
    RESTORE_STARTED,
    RESTORED,
    NONE
}