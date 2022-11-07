package com.kok1337.feature_database_preparation.domain.usecase

import android.content.pm.PackageManager
import com.kok1337.database.annotationmapper.exception.ItemNotFoundException
import com.kok1337.feature_database_preparation.di.qualifier.TermuxPackage
import com.kok1337.feature_database_preparation.domain.model.TermuxState
import com.kok1337.feature_database_preparation.domain.repository.UserRepository
import javax.inject.Inject

class GetTermuxStateUseCase @Inject constructor(
    private val packageManager: PackageManager,
    @TermuxPackage private val termuxPackage: String,
    private val userRepository: UserRepository,
) {
    suspend fun invoke(): TermuxState {
        val isInstalled = try {
            packageManager.getApplicationInfo(termuxPackage, 0).enabled
        } catch (exception: PackageManager.NameNotFoundException) {
            false
        }
        if (!isInstalled) return TermuxState.NOT_INSTALLED
        return try {
            userRepository.getUserId()
            TermuxState.WORKS_CORRECTLY
        } catch (exception: ItemNotFoundException) {
            exception.printStackTrace()
            TermuxState.NOT_WORKS_CORRECTLY
        }
    }
}