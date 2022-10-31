package com.kok1337.feature_database_preparation.domain.model

enum class TermuxState {
    NOT_INSTALLED,
    COPYING_FILES_STARTED,
    FILES_COPIED,
    INSTALLATION_STARTED,
    INSTALLED,
    DELETION_STARTED,
    WORKS_CORRECTLY,
    NOT_WORKS_CORRECTLY
}