package com.kok1337.database.di.component

import com.kok1337.database.di.dep.DatabaseDependencies
import com.kok1337.database.di.module.DatabaseModule
import com.kok1337.database.di.scope.Database
import dagger.Component
import org.springframework.jdbc.core.JdbcTemplate

@[Database Component(
    dependencies = [DatabaseDependencies::class],
    modules = [DatabaseModule::class]
)]
interface DatabaseComponent {
    val jdbcTemplate: JdbcTemplate
}