package com.kok1337.glpmlocal.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kok1337.feature_database_preparation.presentation.fragment.DatabasePreparationFragment
import com.kok1337.glpmlocal.R
import com.kok1337.glpmlocal.app.GlpmLocalApplication
import org.springframework.jdbc.core.JdbcTemplate
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
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


/*        val cmd = "echo 123"
        try {
            val p = Runtime.getRuntime().exec(cmd)
            val stdout: InputStream = p.inputStream
            val reader = BufferedReader(InputStreamReader(stdout))
            p.waitFor()
            if (p.exitValue() != 0) {
                println(p.exitValue())
                Log.e("test", p.exitValue().toString() + " ")
            }
            var s: String
            val stdout_list: MutableList<String> = ArrayList()
            while (reader.readLine().also { s = it } != null) {
                Log.e("test", s)
                s = """
            $s

            """.trimIndent()
                stdout_list.add(s)
            }
        } catch (e: Exception) {
            println(e)
        }*/