package com.kok1337.feature_database_synchronization.presentation.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class DatabaseSynchronizationViewModel : ViewModel() {
    class Factory @Inject constructor(
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DatabaseSynchronizationViewModel::class.java)
            return DatabaseSynchronizationViewModel(

            ) as T
        }
    }
}