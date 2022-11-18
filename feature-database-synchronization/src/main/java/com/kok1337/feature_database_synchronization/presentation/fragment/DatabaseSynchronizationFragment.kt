package com.kok1337.feature_database_synchronization.presentation.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kok1337.feature_database_synchronization.R
import com.kok1337.feature_database_synchronization.databinding.FragmentDatabaseSynchronizationBinding
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
        binding.sendButton.setOnClickListener { fragmentViewModel.uploadBackup() }

        binding.settingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.backupUploadResult
                .onEach {
                    binding.progressBar.progress = it.savedProgress
                }
                .collect()
        }

        binding.makeBackup.setOnClickListener {
            val intent = Intent()
            intent.setClassName("com.termux", "com.termux.app.RunCommandService")
            intent.action = "com.termux.RUN_COMMAND"
            intent.putExtra(
                "com.termux.RUN_COMMAND_PATH",
                "/data/data/com.termux/files/usr/bin/dump_changes"
            )
            intent.putExtra(
                "com.termux.RUN_COMMAND_ARGUMENTS",
                arrayOf("/sdcard/download/backup", "my.backup", "2")
            )

            intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", false)
            requireActivity().startService(intent)
        }
    }
}