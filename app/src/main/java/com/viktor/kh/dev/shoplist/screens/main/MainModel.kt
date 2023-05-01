package com.viktor.kh.dev.shoplist.screens.main


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.viktor.kh.dev.shoplist.utils.APP_PREF
import com.viktor.kh.dev.shoplist.utils.FIRST_LAUNCH_FOR_PRODUCTS
import com.viktor.kh.dev.shoplist.utils.firstLaunchForProductsDB
import com.viktor.kh.dev.shoplist.utils.loadSetting
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class MainModel @Inject  constructor(application: Application) : AndroidViewModel(application) {


    private val app = application
    val appPref = app.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)



    fun init(){
        loadSetting(app)
        checkPreferences()
    }


    private fun checkPreferences(){
        firstLaunchForProductsDB = appPref.getBoolean(FIRST_LAUNCH_FOR_PRODUCTS,false)
        Log.d("dbDebug","firstLaunchForProductsDB = ${firstLaunchForProductsDB}")
    }





}