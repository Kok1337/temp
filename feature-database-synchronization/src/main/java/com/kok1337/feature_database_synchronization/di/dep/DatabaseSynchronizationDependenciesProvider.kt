package com.kok1337.feature_database_synchronization.di.dep

import androidx.annotation.RestrictTo

interface DatabaseSynchronizationDependenciesProvider {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: DatabaseSynchronizationDependencies

    companion object :
        DatabaseSynchronizationDependenciesProvider by DatabaseSynchronizationDependenciesStore
}