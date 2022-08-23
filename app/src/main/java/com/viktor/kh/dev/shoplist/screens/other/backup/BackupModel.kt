package com.viktor.kh.dev.shoplist.screens.other.backup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BackupModel @Inject constructor(application: Application) : AndroidViewModel(application) {


  private val createFileLauncher: ActivityResultLauncher<Intent>? = null


    fun createFile(uri: Uri){
     Log.d("fixLog","uri = ${uri.toString()}" )
    }


   fun readFile(){

   }
}