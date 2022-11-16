package com.kok1337.feature_database_synchronization.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kok1337.feature_database_synchronization.R
import com.kok1337.feature_database_synchronization.databinding.FragmentDatabaseSynchronizationBinding
import dagger.Lazy
import java.io.File
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
    }
}