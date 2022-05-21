package com.hackerton.shimton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackerton.shimton.R
import com.hackerton.shimton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}