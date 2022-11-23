package com.kok1337.feature_database_preparation.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kok1337.feature_database_preparation.R
import com.kok1337.feature_database_preparation.databinding.FragmentDatabasePreparationBinding
import com.kok1337.feature_database_preparation.domain.model.CopyResult
import com.kok1337.feature_database_preparation.domain.model.InstallerState
import com.kok1337.feature_database_preparation.domain.model.InstallerState.*
import com.kok1337.feature_database_preparation.domain.model.TermuxState
import com.kok1337.feature_database_preparation.domain.model.TermuxState.*
import com.kok1337.file.DownloadResult
import com.kok1337.result.DataResult
import com.kok1337.result.ErrorResult
import com.kok1337.result.SuccessResult
import com.kok1337.result.takeSuccess
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DatabasePreparationFragment : Fragment(R.layout.fragment_database_preparation) {
    @Inject
    internal lateinit var databasePreparationViewModelFactory: Lazy<DatabasePreparationViewModel.Factory>
    private val binding by viewBinding(FragmentDatabasePreparationBinding::bind)

    private val fragmentViewModel: DatabasePreparationViewModel by viewModels {
        databasePreparationViewModelFactory.get()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this)[DatabasePreparationComponentViewModel::class.java]
            .databasePreparationComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        fragmentViewModel.updateAllStates()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.downloadInstallerButton.setOnClickListener { fragmentViewModel.downloadInstaller() }
        binding.reinstallTermuxButton.setOnClickListener { fragmentViewModel.deleteTermux() }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.installerState
                .onEach { onInstallerStateChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.termuxState
                .onEach { onTermuxStateChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.installerDownloadResult
                .onEach { onInstallerDownloadResultChanged(it) }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.copyFileResult
                .onEach { updateCopyFileIndicator(it) }
                .collect()
        }
    }

    private var showInstallerIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.downloadInstallerProgressBar.visibility = visibility
            binding.downloadInstallerStatusText.visibility = visibility
        }

    private var showDownloadInstallerButton: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.downloadInstallerButton.visibility = visibility
        }

    private var showCopyFileIndicator: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.termuxCopiedFileText.visibility = visibility
            binding.termuxCopiedFileProgressBar.visibility = visibility
        }

    private var showReinstallTermuxButton: Boolean = false
        set(value) {
            field = value
            val visibility = if (field) View.VISIBLE else View.GONE
            binding.reinstallTermuxButton.visibility = visibility
        }

    private fun onInstallerDownloadResultChanged(result: DataResult<DownloadResult>) {
        when (result) {
            is ErrorResult -> showErrorToast()
            is SuccessResult -> updateInstallerIndicator(result.takeSuccess()!!)
            else -> {}
        }
    }

    private fun onTermuxStateChanged(termuxState: TermuxState) {
        updateTermuxStatus(termuxState)
        updateUI(termuxState)
    }

    private fun onInstallerStateChanged(installerState: InstallerState) {
        updateInstallerStatus(installerState)
        updateUI(installerState)
    }

    private fun updateInstallerStatus(installerState: InstallerState) {
        binding.installerStatusTextView.text = when(installerState) {
            DOWNLOADED -> "Скачан"
            NOT_DOWNLOADED -> "Не скачан"
            NOT_REQUIRED -> "Не требуется"
            DOWNLOAD_IS_IN_PROGRESS -> "Идет загрузка"
            DOWNLOAD_ERROR -> "Ошибка при загрузке"
        }
    }

    private fun updateTermuxStatus(termuxState: TermuxState) {
        binding.termuxStatusTextView.text = when(termuxState) {
            NOT_INSTALLED -> "Не установлен"
            COPYING_FILES_STARTED -> "Копируются файлы"
            FILES_COPIED -> "Файлы скопированы"
            INSTALLATION_STARTED -> "Установка"
            INSTALLED -> "Установлено"
            DELETION_STARTED -> "Удаление"
            WORKS_CORRECTLY -> "Работает корректно"
            NOT_WORKS_CORRECTLY -> "Работает некорректно"
        }
    }

    private fun updateUI(installerState: InstallerState) {
        showInstallerIndicator = when (installerState) {
            DOWNLOAD_IS_IN_PROGRESS -> true
            else -> false
        }
        showDownloadInstallerButton = when (installerState) {
            NOT_DOWNLOADED, DOWNLOAD_ERROR -> true
            else -> false
        }
    }

    private fun updateUI(termuxState: TermuxState) {
        showCopyFileIndicator = when(termuxState) {
            COPYING_FILES_STARTED -> true
            else -> false
        }
        showReinstallTermuxButton = when(termuxState) {
            NOT_WORKS_CORRECTLY -> true
            else -> false
        }
    }

    private fun updateInstallerIndicator(downloadResult: DownloadResult) {
        binding.downloadInstallerStatusText.text = downloadResult.toString()
        binding.downloadInstallerProgressBar.progress = downloadResult.savedProgress
    }

    private fun updateCopyFileIndicator(copyResult: CopyResult) {
        binding.termuxCopiedFileText.text = copyResult.fileName
        binding.termuxCopiedFileProgressBar.progress = copyResult.percent
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Ошибка при загрузке", Toast.LENGTH_SHORT).show()
    }
}