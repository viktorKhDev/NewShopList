package com.viktor.kh.dev.shoplist.screens.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.sortByDate
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class MainModel @Inject  constructor(application: Application) : AndroidViewModel(application) {



    fun loadSetting(context: Context){
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
      sortByDate = sp.getBoolean(context.getString(R.string.sort_by_date),false)
    }


}