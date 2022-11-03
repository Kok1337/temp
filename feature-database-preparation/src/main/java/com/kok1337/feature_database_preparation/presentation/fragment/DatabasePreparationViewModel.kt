package com.kok1337.feature_database_preparation.presentation.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kok1337.feature_database_preparation.domain.model.InstallerState
import com.kok1337.feature_database_preparation.domain.usecase.GetInstallerState
import com.kok1337.feature_database_preparation.domain.usecase.ObserveDownloadInstallerArchiveUseCase
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
    private val getInstallerState: GetInstallerState,
    private val observeDownloadInstallerArchiveUseCase: ObserveDownloadInstallerArchiveUseCase,
) : ViewModel() {

//    private val _termuxState = MutableStateFlow<Result<>>()

    private val _installerState = MutableSharedFlow<InstallerState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val installerState = _installerState.asSharedFlow()

    init {
        getInstallerState()

        viewModelScope.launch(Dispatchers.IO) {
            installerState.onEach { Log.e(javaClass.simpleName, it.name) }.collect()
        }
    }

    fun getInstallerState() = viewModelScope.launch(Dispatchers.IO) {
        _installerState.emit(getInstallerState.invoke())
    }

    fun downloadInstaller(): Flow<DataResult<DownloadResult>> = flow {
        _installerState.emit(InstallerState.DOWNLOAD_IS_IN_PROGRESS)
        try {
            observeDownloadInstallerArchiveUseCase.invoke()
                .onEach { downloadResult -> emit(SuccessResult(downloadResult)) }
                .collect()
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(ErrorResult(exception))
            _installerState.emit(InstallerState.DOWNLOAD_ERROR)
            return@flow
        }
        _installerState.emit(InstallerState.DOWNLOADED)
    }.flowOn(Dispatchers.IO)

    class Factory @Inject constructor(
        private val getInstallerState: GetInstallerState,
        private val observeDownloadInstallerArchiveUseCase: ObserveDownloadInstallerArchiveUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabasePreparationViewModel::class.java)
            return DatabasePreparationViewModel(
                getInstallerState = getInstallerState,
                observeDownloadInstallerArchiveUseCase = observeDownloadInstallerArchiveUseCase,
            ) as T
        }
    }
}