package com.viktor.kh.dev.shoplist.screens.other

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.showToast
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

    fun openPrivacy(activity: Activity){
        try {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://viktorkhdev.github.io/privacy/privacy-policy")
            activity.startActivity(openURL)
        }catch (ex :Exception){
            ex.printStackTrace()
            showToast(activity.getString(R.string.error),activity)
        }

    }

}