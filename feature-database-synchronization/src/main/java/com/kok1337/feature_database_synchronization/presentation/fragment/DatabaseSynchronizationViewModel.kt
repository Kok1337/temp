package com.kok1337.feature_database_synchronization.presentation.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kok1337.feature_database_synchronization.domain.usecase.UploadBackupFileUseCase
import com.kok1337.file.DownloadResult
import com.kok1337.network.domain.model.UploadCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseSynchronizationViewModel (
    private val uploadBackupFileUseCase: UploadBackupFileUseCase,
) : ViewModel() {

    private val _backupUploadResult = MutableStateFlow(DownloadResult.default)
    val backupUploadResult = _backupUploadResult.asStateFlow()

    fun uploadBackup() = viewModelScope.launch(Dispatchers.IO) {
        val uploadCallback: UploadCallback = { savedSize, fileSize ->
            _backupUploadResult.value = DownloadResult(savedSize, fileSize)
        }
        uploadBackupFileUseCase.invoke(uploadCallback)
    }

    class Factory @Inject constructor(
        private val uploadBackupFileUseCase: UploadBackupFileUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabaseSynchronizationViewModel::class.java)
            return DatabaseSynchronizationViewModel(
                uploadBackupFileUseCase = uploadBackupFileUseCase,
            ) as T
        }
    }
}