package com.kok1337.feature_database_synchronization.di.dep

import kotlin.properties.Delegates

object DatabaseSynchronizationDependenciesStore : DatabaseSynchronizationDependenciesProvider {
    override var deps: DatabaseSynchronizationDependencies by Delegates.notNull()
}