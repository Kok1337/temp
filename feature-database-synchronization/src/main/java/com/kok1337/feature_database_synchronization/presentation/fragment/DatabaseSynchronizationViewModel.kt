package com.kok1337.feature_database_synchronization.presentation.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kok1337.feature_database_synchronization.domain.factory.BackupNameFactory
import com.kok1337.feature_database_synchronization.domain.model.UploadBackupState
import com.kok1337.feature_database_synchronization.domain.usecase.CreateBackupUseCase
import com.kok1337.feature_database_synchronization.domain.usecase.ObserveUploadBackupFileUseCase
import com.kok1337.feature_database_synchronization.domain.usecase.WaitBackupCreatingUseCase
import com.kok1337.file.DownloadResult
import com.kok1337.network.domain.model.UploadCallback
import com.kok1337.result.DataResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseSynchronizationViewModel(
    private val observeUploadBackupFileUseCase: ObserveUploadBackupFileUseCase,
    private val createBackupUseCase: CreateBackupUseCase,
    private val backupNameFactory: BackupNameFactory,
    private val waitBackupCreatingUseCase: WaitBackupCreatingUseCase,
) : ViewModel() {

    private val _backupUploadResult =
        MutableStateFlow<DataResult<DownloadResult>>(SuccessResult(DownloadResult.default))
    val backupUploadResult = _backupUploadResult.asStateFlow()

    private val _Upload_backupState = MutableStateFlow(UploadBackupState.NONE)
    val backupState = _Upload_backupState.asStateFlow()

    fun runScript() = viewModelScope.launch(Dispatchers.IO) {
        val fileName = backupNameFactory.create()
        createBackupFile(fileName)
        uploadBackupFile(fileName)
    }

    private suspend fun createBackupFile(fileName: String) {
        _Upload_backupState.value = UploadBackupState.CREATION_STARTED
        createBackupUseCase.invoke(fileName)
        waitBackupCreatingUseCase.invoke(fileName)
        _Upload_backupState.value = UploadBackupState.CREATED
    }

    private suspend fun uploadBackupFile(fileName: String) {
        _Upload_backupState.value = UploadBackupState.UPLOAD_STARTED
        val uploadCallback: UploadCallback = { savedSize, fileSize ->
            _backupUploadResult.value = SuccessResult(DownloadResult(savedSize, fileSize))
        }
        try {
            observeUploadBackupFileUseCase.invoke(fileName, uploadCallback)
        } catch (exception: Exception) {
            exception.printStackTrace()
            _backupUploadResult.value = ErrorResult(exception)
            _Upload_backupState.value = UploadBackupState.UPLOAD_ERROR
            return
        }
        _Upload_backupState.value = UploadBackupState.UPLOADED
    }

    class Factory @Inject constructor(
        private val observeUploadBackupFileUseCase: ObserveUploadBackupFileUseCase,
        private val createBackupUseCase: CreateBackupUseCase,
        private val backupNameFactory: BackupNameFactory,
        private val waitBackupCreatingUseCase: WaitBackupCreatingUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabaseSynchronizationViewModel::class.java)
            return DatabaseSynchronizationViewModel(
                observeUploadBackupFileUseCase = observeUploadBackupFileUseCase,
                createBackupUseCase = createBackupUseCase,
                backupNameFactory = backupNameFactory,
                waitBackupCreatingUseCase = waitBackupCreatingUseCase,
            ) as T
        }
    }
}