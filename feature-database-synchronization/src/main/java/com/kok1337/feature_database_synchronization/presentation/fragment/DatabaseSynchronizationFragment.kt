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
        binding.createAndUploadBackupButton.setOnClickListener { fragmentViewModel.runScript() }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.backupUploadResult
                .onEach { onBackupUploadResultChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.backupState
                .onEach { onBackupStateChanged(it) }
                .collect()
        }
    }

    private var showBackupStatusTextView: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.backupStatusTextView.visibility = visibility
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

    private fun onBackupStateChanged(uploadBackupState: UploadBackupState) {
        updateBackupStatus(uploadBackupState)
        updateUI(uploadBackupState)
    }

    private fun onBackupUploadResultChanged(result: DataResult<DownloadResult>) {
        when (result) {
            is ErrorResult -> showErrorToast()
            is SuccessResult -> updateUploadIndicator(result.takeSuccess()!!)
            else -> {}
        }
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Ошибка при отправке на сервер", Toast.LENGTH_SHORT).show()
    }

    private fun updateUploadIndicator(uploadResult: DownloadResult) {
        binding.uploadBackupProgressBar.progress = uploadResult.savedProgress
        binding.uploadBackupStatusText.text = uploadResult.toString()
    }

    private fun updateBackupStatus(uploadBackupState: UploadBackupState) {
        binding.backupStatusTextView.text = when(uploadBackupState) {
            CREATION_STARTED -> "Backup создается"
            CREATED -> "Backup создан"
            UPLOAD_STARTED -> "Идет загрузка"
            UPLOADED -> "Загружен на сервер"
            UPLOAD_ERROR -> "Ошибка при отправке"
            NONE -> ""
        }
    }

    private fun updateUI(uploadBackupState: UploadBackupState) {
        showBackupStatusTextView = uploadBackupState != NONE
        showCreateBackupIndicator = uploadBackupState == CREATION_STARTED
        showUploadBackupIndicator = when(uploadBackupState) {
            UPLOAD_STARTED, UPLOADED -> true
            else -> false
        }
    }
}

/*binding.settingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivity(intent)
        }*/