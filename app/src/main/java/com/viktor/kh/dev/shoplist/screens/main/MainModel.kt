package com.viktor.kh.dev.shoplist.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.MainActivityBinding
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class MainModel @Inject  constructor(application: Application) : AndroidViewModel(application) {


  /* fun init(binding: MainActivityBinding){
      binding.bottomNavView.setOnItemReselectedListener {
         when(it.itemId){
            R.id.lists_item_nav -> {}
            R.id.recipes_item_nav -> {}
            R.id.other_nav -> {}
         }
      }


   }*/

}