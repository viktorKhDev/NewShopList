package com.viktor.kh.dev.shoplist.screens.other

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherModel @Inject constructor(application: Application) : AndroidViewModel(application) {




    fun openSetting(navController: NavController){
        navController.navigate(R.id.action_otherFragment_to_settingsFragment)
    }

    fun openBackup(navController: NavController){
        navController.navigate(R.id.action_otherFragment_to_backupFragment2)
    }

    fun openSupport(navController: NavController){
        navController.navigate(R.id.action_otherFragment_to_infoFragment2)
    }

}