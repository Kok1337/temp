package com.kok1337.feature_database_preparation.domain.usecase

import com.kok1337.feature_database_preparation.domain.model.TermuxState
import javax.inject.Inject

class InstallerIsRequiredUseCase @Inject constructor(
     private val getTermuxStateUseCase: GetTermuxStateUseCase,
) {
    suspend fun invoke(): Boolean {
        val termuxState = getTermuxStateUseCase.invoke()
        if (termuxState == TermuxState.WORKS_CORRECTLY) return false
        return true
    }
}