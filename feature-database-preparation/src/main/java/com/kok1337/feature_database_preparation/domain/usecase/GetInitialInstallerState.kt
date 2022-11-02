package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.domain.model.InstallerState
import javax.inject.Inject

class GetInitialInstallerState @Inject constructor(

) {
    suspend fun invoke(): InstallerState {
        return InstallerState.NOT_DOWNLOADED
    }
}