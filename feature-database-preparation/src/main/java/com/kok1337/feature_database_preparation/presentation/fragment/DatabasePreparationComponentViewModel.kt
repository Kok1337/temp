package com.kok1337.feature_database_preparation.presentation.fragment

import androidx.lifecycle.ViewModel
import com.kok1337.feature_database_preparation.di.component.DaggerDatabasePreparationComponent
import com.kok1337.feature_database_preparation.di.dep.DatabasePreparationDependenciesProvider

internal class DatabasePreparationComponentViewModel : ViewModel() {
    val databasePreparationComponent = DaggerDatabasePreparationComponent.builder()
        .deps(DatabasePreparationDependenciesProvider.deps)
        .build()
}