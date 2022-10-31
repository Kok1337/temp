package com.kok1337.feature_database_preparation.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.kok1337.feature_database_preparation.R
import dagger.Lazy
import javax.inject.Inject

class DatabasePreparationFragment : Fragment(R.layout.fragment_database_preparation) {
    @Inject
    internal lateinit var databasePreparationViewModelFactory: Lazy<DatabasePreparationViewModel.Factory>

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
        fragmentViewModel.print()
    }
}