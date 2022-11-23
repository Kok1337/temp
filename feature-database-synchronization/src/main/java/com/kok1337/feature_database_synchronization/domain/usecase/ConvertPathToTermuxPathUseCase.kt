package com.kok1337.feature_database_synchronization.domain.usecase

import android.annotation.SuppressLint
import javax.inject.Inject

class ConvertPathToTermuxPathUseCase @Inject constructor() {
    @SuppressLint("SdCardPath")
    fun invoke(convertiblePath: String): String {
        return convertiblePath.replace("/storage/emulated/0", "/sdcard")
    }
}