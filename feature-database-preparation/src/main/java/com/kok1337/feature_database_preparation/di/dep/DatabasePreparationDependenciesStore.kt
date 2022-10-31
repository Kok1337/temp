package com.kok1337.feature_database_preparation.di.dep

import kotlin.properties.Delegates

object DatabasePreparationDependenciesStore : DatabasePreparationDependenciesProvider {
    override var deps: DatabasePreparationDependencies by Delegates.notNull()
}