package com.kok1337.glpmlocal.di.component

import android.content.Context
import com.kok1337.glpmlocal.presentation.activity.MainActivity
import com.kok1337.glpmlocal.di.scope.App
import dagger.BindsInstance
import dagger.Component
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit

@[App Component]
interface AppComponent {
    val context: Context
    val jdbcTemplate: JdbcTemplate
    val retrofit: Retrofit

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        @BindsInstance
        fun jdbcTemplate(jdbcTemplate: JdbcTemplate): Builder
        @BindsInstance
        fun retrofit(retrofit: Retrofit): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}