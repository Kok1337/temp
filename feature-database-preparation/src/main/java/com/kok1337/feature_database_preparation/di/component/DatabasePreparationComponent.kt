package com.kok1337.feature_database_preparation.di.component

import com.kok1337.feature_database_preparation.di.dep.DatabasePreparationDependencies
import com.kok1337.feature_database_preparation.di.scope.FeatureDatabasePreparation
import com.kok1337.feature_database_preparation.presentation.fragment.DatabasePreparationFragment
import dagger.Component

@[FeatureDatabasePreparation Component(
    dependencies = [DatabasePreparationDependencies::class],
)]
internal interface DatabasePreparationComponent {
    fun inject(databasePreparationFragment: DatabasePreparationFragment)

    @Component.Builder
    interface Builder {
        fun deps(databasePreparationDependencies: DatabasePreparationDependencies): Builder
        fun build(): DatabasePreparationComponent
    }
}