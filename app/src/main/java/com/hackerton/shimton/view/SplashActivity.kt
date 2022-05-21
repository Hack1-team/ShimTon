package com.hackerton.shimton.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hackerton.shimton.databinding.ActivitySplashBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySplashBinding

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        launch {
            delay(2000L)
            startActivity(Intent(this@SplashActivity,HomeActivity::class.java).apply {
                finish()
            })
        }

        //push
//        if (intent?.extras != null) {
//            for (key: String in intent!!.extras!!.keySet()) {
//                val value = intent!!.extras!!.get(key)
//                if(value.toString().contains("코멘트가 도착하였습니다.")) {
//                    Log.d("pushaaaaaa", "$value Key: " + key + "           Value: " + value);
//                    val yesterDay = DateConverter.getCodaToday().minusDays(1)
//                    pushDate = DateConverter.ymdFormat(yesterDay)
//                }
//            }
//        }
    }


}