package com.kok1337.feature_database_preparation.di.dep

import androidx.annotation.RestrictTo

interface DatabasePreparationDependenciesProvider {
    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: DatabasePreparationDependencies

    companion object :
        DatabasePreparationDependenciesProvider by DatabasePreparationDependenciesStore
}