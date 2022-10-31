package com.kok1337.database.di.module

import com.kok1337.database.di.dep.DatabaseDependencies
import com.kok1337.database.di.qualifier.*
import com.kok1337.database.di.scope.Database
import dagger.Module
import dagger.Provides
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Module
class DatabaseModule {
    @[DbHost Provides]
    fun provideHost(databaseDeps: DatabaseDependencies): String = databaseDeps.host

    @[DbPort Provides]
    fun providePort(databaseDeps: DatabaseDependencies): String = databaseDeps.port

    @[DbName Provides]
    fun provideName(databaseDeps: DatabaseDependencies): String = databaseDeps.name

    @[DbUsername Provides]
    fun provideUsername(databaseDeps: DatabaseDependencies): String = databaseDeps.username

    @[DbPassword Provides]
    fun providePassword(databaseDeps: DatabaseDependencies): String = databaseDeps.password

    @[DbDriverName Provides]
    fun provideDriverName(databaseDeps: DatabaseDependencies): String = databaseDeps.driverName

    @[Provides]
    fun provideDataSource(
        @DbHost host: String,
        @DbPort port: String,
        @DbName name: String,
        @DbUsername username: String,
        @DbPassword password: String,
        @DbDriverName driverName: String
    ): DataSource {
        val url = "jdbc:postgresql://${host}:$port}/${name}"
        val dataSource = DriverManagerDataSource(url, username, password)
        dataSource.setDriverClassName(driverName)
        return dataSource
    }

    @[Database Provides]
    fun provideJdbcTemplate(dataSource: DataSource): JdbcTemplate = JdbcTemplate(dataSource)
}