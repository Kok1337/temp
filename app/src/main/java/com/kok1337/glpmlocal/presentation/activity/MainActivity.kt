package com.kok1337.glpmlocal.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kok1337.feature_database_preparation.presentation.fragment.DatabasePreparationFragment
import com.kok1337.glpmlocal.R
import com.kok1337.glpmlocal.app.GlpmLocalApplication
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var jdbcTemplate: JdbcTemplate
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as GlpmLocalApplication).appComponent.inject(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DatabasePreparationFragment())
                .commitNow()
        }
    }
}