package com.kok1337.feature_database_synchronization.di.component

import com.kok1337.feature_database_synchronization.di.scope.FeatureDatabaseSynchronization
import com.kok1337.feature_database_synchronization.di.dep.DatabaseSynchronizationDependencies
import com.kok1337.feature_database_synchronization.di.module.DatabaseSynchronizationBindsModule
import com.kok1337.feature_database_synchronization.di.module.DatabaseSynchronizationProvidesModule
import com.kok1337.feature_database_synchronization.presentation.fragment.DatabaseSynchronizationFragment
import dagger.Component

@[FeatureDatabaseSynchronization Component(
    dependencies = [DatabaseSynchronizationDependencies::class],
    modules = [
        DatabaseSynchronizationBindsModule::class,
        DatabaseSynchronizationProvidesModule::class,
    ]
)]
internal interface DatabaseSynchronizationComponent {
    fun inject(databasePreparationFragment: DatabaseSynchronizationFragment)

    @Component.Builder
    interface Builder {
        fun deps(databasePreparationDependencies: DatabaseSynchronizationDependencies): Builder
        fun build(): DatabaseSynchronizationComponent
    }
}