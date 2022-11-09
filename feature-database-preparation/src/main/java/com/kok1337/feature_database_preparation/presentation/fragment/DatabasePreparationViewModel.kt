package com.kok1337.feature_database_preparation.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kok1337.feature_database_preparation.domain.model.CopyResult
import com.kok1337.feature_database_preparation.domain.model.InstallerState
import com.kok1337.feature_database_preparation.domain.model.InstallerState.*
import com.kok1337.feature_database_preparation.domain.model.TermuxState
import com.kok1337.feature_database_preparation.domain.usecase.*
import com.kok1337.file.DownloadResult
import com.kok1337.result.DataResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabasePreparationViewModel(
    private val getInstallerStateUseCase: GetInstallerStateUseCase,
    private val getTermuxStateUseCase: GetTermuxStateUseCase,
    private val observeDownloadInstallerArchiveUseCase: ObserveDownloadInstallerArchiveUseCase,
    private val observeCopyFilesFromInstallerArchiveUseCase: ObserveCopyFilesFromInstallerArchiveUseCase,
    private val installTermuxUseCase: InstallTermuxUseCase,
    private val deleteCopiedFilesUseCase: DeleteCopiedFilesUseCase,
    private val deleteTermuxUseCase: DeleteTermuxUseCase,
) : ViewModel() {
    companion object {
        private val LOG_TAG = DatabasePreparationViewModel::class.java.simpleName
    }

    private val _installerState =
        MutableSharedFlow<InstallerState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val installerState = _installerState.asSharedFlow()

    private val _termuxState =
        MutableSharedFlow<TermuxState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val termuxState = _termuxState.asSharedFlow()

    private val _installerDownloadResult =
        MutableStateFlow<DataResult<DownloadResult>>(SuccessResult(DownloadResult.default))
    val installerDownloadResult = _installerDownloadResult.asStateFlow()

    private val _copyFileResult =
        MutableStateFlow(CopyResult.default)
    val copyFileResult = _copyFileResult.asStateFlow()

    init {
        updateAllStates()

        viewModelScope.launch(Dispatchers.IO) {
            installerState.onEach { installerState ->
                Log.e(LOG_TAG, installerState.name)
                when (installerState) {
                    NOT_DOWNLOADED -> downloadInstaller()
                    DOWNLOADED -> {
                        val termuxState = termuxState.first()
                        if (termuxState != TermuxState.NOT_INSTALLED) return@onEach
                        Log.e(LOG_TAG, "start copy files")
                        copyInstallerFiles()
                    }
                    else -> {}
                }
            }.collect()
        }

        viewModelScope.launch(Dispatchers.IO) {
            termuxState.onEach { termuxState ->
                Log.e(LOG_TAG, termuxState.name)
                when (termuxState) {
                    TermuxState.FILES_COPIED -> installTermux()
                    TermuxState.WORKS_CORRECTLY, TermuxState.NOT_WORKS_CORRECTLY -> deleteCopiedFiles()
                    else -> {}
                }
            }.collect()
        }
    }

    fun updateAllStates() {
        getInstallerState()
        getTermuxState()
    }

    private fun getInstallerState() = viewModelScope.launch(Dispatchers.IO) {
        if (installerState.replayCache.isNotEmpty()) {
            if (installerState.first() == DOWNLOAD_IS_IN_PROGRESS) return@launch
        }
        _installerState.emit(getInstallerStateUseCase.invoke())
    }

    private fun getTermuxState() = viewModelScope.launch(Dispatchers.IO) {
        if (termuxState.replayCache.isNotEmpty()) {
            if (termuxState.first() == TermuxState.COPYING_FILES_STARTED) return@launch
            if (termuxState.first() == TermuxState.FILES_COPIED) return@launch
        }
        _termuxState.emit(getTermuxStateUseCase.invoke())
    }

    private fun deleteCopiedFiles() = viewModelScope.launch(Dispatchers.IO) {
        deleteCopiedFilesUseCase.invoke()
    }

    fun downloadInstaller() = viewModelScope.launch(Dispatchers.IO) {
        _installerState.emit(DOWNLOAD_IS_IN_PROGRESS)
        try {
            observeDownloadInstallerArchiveUseCase.invoke()
                .onEach { downloadResult ->
                    _installerDownloadResult.emit(SuccessResult(downloadResult))
                }
                .collect()
        } catch (exception: Exception) {
            exception.printStackTrace()
            _installerDownloadResult.emit(ErrorResult(exception))
            _installerState.emit(DOWNLOAD_ERROR)
            return@launch
        }
        _installerState.emit(DOWNLOADED)
    }

    private fun copyInstallerFiles() = viewModelScope.launch(Dispatchers.IO) {
        _termuxState.emit(TermuxState.COPYING_FILES_STARTED)
        observeCopyFilesFromInstallerArchiveUseCase.invoke()
            .onEach { _copyFileResult.emit(it) }
            .collect()
        _termuxState.emit(TermuxState.FILES_COPIED)
    }

    fun deleteTermux() = viewModelScope.launch(Dispatchers.IO) {
        _termuxState.emit(TermuxState.DELETION_STARTED)
        deleteTermuxUseCase.invoke()
    }

    private fun installTermux() = viewModelScope.launch(Dispatchers.IO) {
        _termuxState.emit(TermuxState.INSTALLATION_STARTED)
        installTermuxUseCase.invoke()
    }

    class Factory @Inject constructor(
        private val getInstallerStateUseCase: GetInstallerStateUseCase,
        private val getTermuxStateUseCase: GetTermuxStateUseCase,
        private val observeDownloadInstallerArchiveUseCase: ObserveDownloadInstallerArchiveUseCase,
        private val observeCopyFilesFromInstallerArchiveUseCase: ObserveCopyFilesFromInstallerArchiveUseCase,
        private val installTermuxUseCase: InstallTermuxUseCase,
        private val deleteCopiedFilesUseCase: DeleteCopiedFilesUseCase,
        private val deleteTermuxUseCase: DeleteTermuxUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabasePreparationViewModel::class.java)
            return DatabasePreparationViewModel(
                getInstallerStateUseCase = getInstallerStateUseCase,
                getTermuxStateUseCase = getTermuxStateUseCase,
                observeDownloadInstallerArchiveUseCase = observeDownloadInstallerArchiveUseCase,
                observeCopyFilesFromInstallerArchiveUseCase = observeCopyFilesFromInstallerArchiveUseCase,
                installTermuxUseCase = installTermuxUseCase,
                deleteCopiedFilesUseCase = deleteCopiedFilesUseCase,
                deleteTermuxUseCase = deleteTermuxUseCase,
            ) as T
        }
    }
}