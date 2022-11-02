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
import com.kok1337.result.ErrorResult
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { tryDownloadInstallerArchive() }
        binding.imageButton.isEnabled = false

        lifecycleScope.launchWhenStarted {
            fragmentViewModel.installerState
                .onEach { binding.status.text = it.name }
                .collect()
        }
    }

    private fun tryDownloadInstallerArchive() = lifecycleScope.launchWhenStarted {
        fragmentViewModel.downloadInstaller()
            .onEach {
                if (it is ErrorResult) Toast.makeText(
                    requireContext(),
                    "${it.exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                else binding.result.text = it.takeSuccess().toString()
            }
            .collect()
    }
}