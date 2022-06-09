package com.viktor.kh.dev.shoplist.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.viktor.kh.dev.shoplist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }


}