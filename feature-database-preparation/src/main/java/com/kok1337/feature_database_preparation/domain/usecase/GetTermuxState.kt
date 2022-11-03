package com.kok1337.feature_database_preparation.domain.usecase

import android.content.pm.PackageManager
import com.kok1337.feature_database_preparation.di.qualifier.TermuxPackage
import com.kok1337.feature_database_preparation.domain.model.TermuxState
import javax.inject.Inject

class GetTermuxState @Inject constructor(
    private val packageManager: PackageManager,
    @TermuxPackage private val termuxPackage: String,
){
    suspend fun invoke(): TermuxState {
        val isInstalled = try {
            packageManager.getApplicationInfo(termuxPackage, 0).enabled
        } catch (exception: PackageManager.NameNotFoundException)  { false }
        if (!isInstalled) return TermuxState.NOT_INSTALLED
        return TermuxState.INSTALLED
    }
}