package com.aqsa.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)


        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main){
                finish()
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))

            }
        }

    }
}