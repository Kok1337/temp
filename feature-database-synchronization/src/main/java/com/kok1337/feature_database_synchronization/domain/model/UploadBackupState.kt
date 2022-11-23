package com.kok1337.feature_database_synchronization.domain.model

enum class UploadBackupState {
    CREATION_STARTED,
    CREATED,
    UPLOAD_STARTED,
    UPLOADED,
    UPLOAD_ERROR,
    NONE
}