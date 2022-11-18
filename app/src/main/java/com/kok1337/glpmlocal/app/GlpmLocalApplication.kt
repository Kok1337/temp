package com.kok1337.glpmlocal.app

import android.app.Application
import android.content.Context
import com.kok1337.database.di.component.DaggerDatabaseComponent
import com.kok1337.database.di.dep.DatabaseDependencies
import com.kok1337.feature_database_preparation.di.dep.DatabasePreparationDependencies
import com.kok1337.feature_database_preparation.di.dep.DatabasePreparationDependenciesStore
import com.kok1337.feature_database_synchronization.di.dep.DatabaseSynchronizationDependencies
import com.kok1337.feature_database_synchronization.di.dep.DatabaseSynchronizationDependenciesStore
import com.kok1337.glpmlocal.BuildConfig
import com.kok1337.glpmlocal.di.component.AppComponent
import com.kok1337.glpmlocal.di.component.DaggerAppComponent
import com.kok1337.network.di.component.DaggerNetworkComponent
import com.kok1337.network.di.dep.NetworkDependencies
import okhttp3.Cache
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit
import java.util.*

class GlpmLocalApplication : Application() {
    companion object {
        private const val CONFIG_FILE_NAME = "config.properties"

        // NET
        private const val NET_BASE_URL = "net_base_url"
        private const val NET_CACHE_SIZE = "net_cache_size"

        // DATABASE
        private const val DB_HOST = "db_host"
        private const val DB_PORT = "db_port"
        private const val DB_NAME = "db_name"
        private const val DB_USER = "db_user"
        private const val DB_PASSWORD = "db_password"
        private const val DB_DRIVER_NAME = "db_driver_name"
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        val databaseComponent = DaggerDatabaseComponent.builder()
            .databaseDependencies(DatabaseDependenciesImpl())
            .build()

        val networkComponent = DaggerNetworkComponent.builder()
            .networkDependencies(NetworkDependenciesImpl())
            .build()

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .jdbcTemplate(databaseComponent.jdbcTemplate)
            .retrofit(networkComponent.retrofit)
            .build()

        DatabasePreparationDependenciesStore.deps =
            DatabasePreparationDependenciesImpl(appComponent)

        DatabaseSynchronizationDependenciesStore.deps =
            DatabaseSynchronizationDependenciesImpl(appComponent)
    }

    private fun property(key: String): String {
        val properties = Properties()
        val inStream = assets.open(CONFIG_FILE_NAME)
        properties.load(inStream)
        return properties.getProperty(key)
    }

    inner class DatabaseDependenciesImpl : DatabaseDependencies {
        override val host: String get() = property(DB_HOST)
        override val port: String get() = property(DB_PORT)
        override val name: String get() = property(DB_NAME)
        override val username: String get() = property(DB_USER)
        override val password: String get() = property(DB_PASSWORD)
        override val driverName: String get() = property(DB_DRIVER_NAME)
    }

    inner class NetworkDependenciesImpl : NetworkDependencies {
        private val maxCacheSizeInMb: Long get() = property(NET_CACHE_SIZE).toLong() * 1024 * 1024
        override val baseUrl: String get() = property(NET_BASE_URL)
        override val cache: Cache get() = Cache(cacheDir, maxCacheSizeInMb)
    }

    inner class DatabasePreparationDependenciesImpl(
        private val component: AppComponent,
    ) : DatabasePreparationDependencies {
        override val context: Context get() = component.context
        override val jdbcTemplate: JdbcTemplate get() = component.jdbcTemplate
        override val retrofit: Retrofit get() = component.retrofit
        override val applicationId: String get() = BuildConfig.APPLICATION_ID
    }

    inner class DatabaseSynchronizationDependenciesImpl(
        private val component: AppComponent,
    ): DatabaseSynchronizationDependencies {
        override val context: Context get() = component.context
        override val jdbcTemplate: JdbcTemplate get() = component.jdbcTemplate
        override val retrofit: Retrofit get() = component.retrofit
    }
}