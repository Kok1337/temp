package com.kok1337.feature_database_synchronization.presentation.fragment

import androidx.lifecycle.ViewModel
import com.kok1337.feature_database_synchronization.di.component.DaggerDatabaseSynchronizationComponent
import com.kok1337.feature_database_synchronization.di.dep.DatabaseSynchronizationDependenciesStore

internal class DatabaseSynchronizationComponentViewModel : ViewModel() {
    val databaseSynchronizationComponent = DaggerDatabaseSynchronizationComponent.builder()
        .deps(DatabaseSynchronizationDependenciesStore.deps)
        .build()
}