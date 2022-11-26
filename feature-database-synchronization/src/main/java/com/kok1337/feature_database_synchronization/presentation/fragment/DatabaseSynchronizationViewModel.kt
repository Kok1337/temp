package com.kok1337.feature_database_synchronization.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kok1337.feature_database_synchronization.domain.factory.BackupNameFactory
import com.kok1337.feature_database_synchronization.domain.model.RestoreBackupState
import com.kok1337.feature_database_synchronization.domain.model.UploadBackupState
import com.kok1337.feature_database_synchronization.domain.usecase.*
import com.kok1337.file.DownloadResult
import com.kok1337.network.domain.model.UploadCallback
import com.kok1337.result.DataResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseSynchronizationViewModel(
    private val observeUploadBackupFileUseCase: ObserveUploadBackupFileUseCase,
    private val createBackupUseCase: CreateBackupUseCase,
    private val backupNameFactory: BackupNameFactory,
    private val waitBackupCreatingUseCase: WaitBackupCreatingUseCase,
    private val observeDownloadBackupUseCase: ObserveDownloadBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
) : ViewModel() {

    private val _backupUploadResult =
        MutableStateFlow<DataResult<DownloadResult>>(SuccessResult(DownloadResult.default))
    val backupUploadResult = _backupUploadResult.asStateFlow()

    private val _backupDownloadResult =
        MutableStateFlow<DataResult<DownloadResult>>(SuccessResult(DownloadResult.default))
    val backupDownloadResult = _backupDownloadResult.asStateFlow()

    private val _uploadBackupState = MutableStateFlow(UploadBackupState.NONE)
    val uploadBackupState = _uploadBackupState.asStateFlow()

    private val _restoreBackupState = MutableStateFlow(RestoreBackupState.NONE)
    val restoreBackupState = _restoreBackupState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            restoreBackupState.onEach { restoreState ->
                when (restoreState) {
                    RestoreBackupState.DOWNLOADED -> restoreBackup()
                    else -> {}
                }
            }.collect()
        }
    }

    fun sendBackup() = viewModelScope.launch(Dispatchers.IO) {
        val fileName = backupNameFactory.create()
        createBackupFile(fileName)
        uploadBackupFile(fileName)
    }

    private suspend fun createBackupFile(fileName: String) {
        _uploadBackupState.value = UploadBackupState.CREATION_STARTED
        createBackupUseCase.invoke(fileName)
        waitBackupCreatingUseCase.invoke(fileName)
        _uploadBackupState.value = UploadBackupState.CREATED
    }

    private suspend fun uploadBackupFile(fileName: String) {
        _uploadBackupState.value = UploadBackupState.UPLOAD_STARTED
        val uploadCallback: UploadCallback = { savedSize, fileSize ->
            _backupUploadResult.value = SuccessResult(DownloadResult(savedSize, fileSize))
        }
        try {
            observeUploadBackupFileUseCase.invoke(fileName, uploadCallback)
        } catch (exception: Exception) {
            exception.printStackTrace()
            _backupUploadResult.value = ErrorResult(exception)
            _uploadBackupState.value = UploadBackupState.UPLOAD_ERROR
            return
        }
        _uploadBackupState.value = UploadBackupState.UPLOADED
    }

    fun downloadBackup() = viewModelScope.launch(Dispatchers.IO) {
        _restoreBackupState.value = RestoreBackupState.DOWNLOAD_STARTED
        try {
            observeDownloadBackupUseCase.invoke()
                .onEach { downloadResult ->
                    _backupDownloadResult.emit(SuccessResult(downloadResult))
                }
                .collect()
        } catch (exception: Exception) {
            exception.printStackTrace()
            _backupDownloadResult.emit(ErrorResult(exception))
            _restoreBackupState.emit(RestoreBackupState.DOWNLOAD_ERROR)
            return@launch
        }
        _restoreBackupState.value = RestoreBackupState.DOWNLOADED
    }

    private fun restoreBackup() {
        _restoreBackupState.value = RestoreBackupState.RESTORE_STARTED
        restoreBackupUseCase.invoke()
    }

    class Factory @Inject constructor(
        private val observeUploadBackupFileUseCase: ObserveUploadBackupFileUseCase,
        private val createBackupUseCase: CreateBackupUseCase,
        private val backupNameFactory: BackupNameFactory,
        private val waitBackupCreatingUseCase: WaitBackupCreatingUseCase,
        private val observeDownloadBackupUseCase: ObserveDownloadBackupUseCase,
        private val restoreBackupUseCase: RestoreBackupUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabaseSynchronizationViewModel::class.java)
            return DatabaseSynchronizationViewModel(
                observeUploadBackupFileUseCase = observeUploadBackupFileUseCase,
                createBackupUseCase = createBackupUseCase,
                backupNameFactory = backupNameFactory,
                waitBackupCreatingUseCase = waitBackupCreatingUseCase,
                observeDownloadBackupUseCase = observeDownloadBackupUseCase,
                restoreBackupUseCase = restoreBackupUseCase,
            ) as T
        }
    }
}