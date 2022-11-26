package com.kok1337.feature_database_synchronization.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kok1337.feature_database_synchronization.R
import com.kok1337.feature_database_synchronization.databinding.FragmentDatabaseSynchronizationBinding
import com.kok1337.feature_database_synchronization.domain.model.RestoreBackupState
import com.kok1337.feature_database_synchronization.domain.model.RestoreBackupState.*
import com.kok1337.feature_database_synchronization.domain.model.UploadBackupState
import com.kok1337.feature_database_synchronization.domain.model.UploadBackupState.*
import com.kok1337.file.DownloadResult
import com.kok1337.result.DataResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.SuccessResult
import com.kok1337.result.takeSuccess
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class DatabaseSynchronizationFragment : Fragment(R.layout.fragment_database_synchronization) {
    @Inject
    internal lateinit var databaseSynchronizationViewModelFactory: Lazy<DatabaseSynchronizationViewModel.Factory>
    private val binding by viewBinding(FragmentDatabaseSynchronizationBinding::bind)

    private val fragmentViewModel: DatabaseSynchronizationViewModel by viewModels {
        databaseSynchronizationViewModelFactory.get()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this)[DatabaseSynchronizationComponentViewModel::class.java]
            .databaseSynchronizationComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createAndUploadBackupButton.setOnClickListener { fragmentViewModel.sendBackup() }
        binding.downloadAndRestoreBackupButton.setOnClickListener { fragmentViewModel.downloadBackup() }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.backupUploadResult
                .onEach { onBackupUploadResultChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.uploadBackupState
                .onEach { onBackupStateChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.backupDownloadResult
                .onEach { onBackupDownloadResultChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.restoreBackupState
                .onEach { onBackupStateChanged(it) }
                .collect()
        }
    }

    private var showUploadBackupStatusTextView: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.uploadBackupStatusTextView.visibility = visibility
        }

    private var showCreateBackupIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.createBackupProgressBar.visibility = visibility
        }

    private var showUploadBackupIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.uploadBackupProgressBar.visibility = visibility
            binding.uploadBackupStatusText.visibility = visibility
        }

    private var enableCreateAndUploadBackupButton: Boolean = true
        set(value) {
            field = value
            binding.createAndUploadBackupButton.isEnabled = field
        }

    private var showRestoreBackupStatusTextView: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.restoreBackupStatusTextView.visibility = visibility
        }

    private var showDownloadBackupIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.downloadBackupProgressBar.visibility = visibility
            binding.downloadBackupStatusText.visibility = visibility
        }

    private var showRestoreBackupIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.restoreBackupProgressBar.visibility = visibility
        }

    private var enableDownloadAndRestoreBackupButton: Boolean = true
        set(value) {
            field = value
            binding.downloadAndRestoreBackupButton.isEnabled = field
        }

    private fun onBackupStateChanged(uploadBackupState: UploadBackupState) {
        updateBackupStatus(uploadBackupState)
        updateUI(uploadBackupState)
    }

    private fun onBackupUploadResultChanged(result: DataResult<DownloadResult>) {
        when (result) {
            is ErrorResult -> showErrorToast("Ошибка при отправке на сервер")
            is SuccessResult -> updateUploadIndicator(result.takeSuccess()!!)
            else -> {}
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUploadIndicator(uploadResult: DownloadResult) {
        binding.uploadBackupProgressBar.progress = uploadResult.savedProgress
        binding.uploadBackupStatusText.text = uploadResult.toString()
    }

    private fun updateBackupStatus(uploadBackupState: UploadBackupState) {
        binding.uploadBackupStatusTextView.text = when (uploadBackupState) {
            CREATION_STARTED -> "Backup создается"
            CREATED -> "Backup создан"
            UPLOAD_STARTED -> "Идет загрузка"
            UPLOADED -> "Загружен на сервер"
            UPLOAD_ERROR -> "Ошибка при отправке"
            UploadBackupState.NONE -> ""
        }
    }


    private fun onBackupStateChanged(restoreBackupState: RestoreBackupState) {
        updateBackupStatus(restoreBackupState)
        updateUI(restoreBackupState)
    }

    private fun onBackupDownloadResultChanged(result: DataResult<DownloadResult>) {
        when (result) {
            is ErrorResult -> showErrorToast("Ошибка при загрузке")
            is SuccessResult -> updateDownloadIndicator(result.takeSuccess()!!)
            else -> {}
        }
    }

    private fun updateDownloadIndicator(downloadResult: DownloadResult) {
        binding.downloadBackupProgressBar.progress = downloadResult.savedProgress
        binding.downloadBackupStatusText.text = downloadResult.toString()
    }

    private fun updateBackupStatus(restoreBackupState: RestoreBackupState) {
        binding.restoreBackupStatusTextView.text = when (restoreBackupState) {
            DOWNLOAD_STARTED -> "Загрузка обновлений"
            DOWNLOADED -> "Обновления скачены"
            DOWNLOAD_ERROR -> "Ошибка при загрузке"
            RESTORE_STARTED -> "Установка обновлений"
            RESTORED -> "Обновления установлены"
            RestoreBackupState.NONE -> ""
        }
    }

    private fun updateUI(uploadBackupState: UploadBackupState) {
        showUploadBackupStatusTextView = uploadBackupState != UploadBackupState.NONE
        showCreateBackupIndicator = uploadBackupState == CREATION_STARTED
        showUploadBackupIndicator = when (uploadBackupState) {
            UPLOAD_STARTED, UPLOADED -> true
            else -> false
        }
        enableCreateAndUploadBackupButton = when (uploadBackupState) {
            UploadBackupState.NONE, UPLOAD_ERROR, UPLOADED -> true
            else -> false
        }
    }

    private fun updateUI(restoreBackupState: RestoreBackupState) {
        showRestoreBackupStatusTextView = restoreBackupState != RestoreBackupState.NONE
        showDownloadBackupIndicator = restoreBackupState == DOWNLOAD_STARTED
        showRestoreBackupIndicator = restoreBackupState == RESTORE_STARTED
        enableDownloadAndRestoreBackupButton = when (restoreBackupState) {
            RestoreBackupState.NONE, DOWNLOAD_ERROR, RESTORED -> true
            else -> false
        }
    }
}