package com.viktor.kh.dev.shoplist.screens.main



import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.loadSetting
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView : BottomNavigationView
    private val model: MainModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        loadSetting(this)
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavView =  findViewById(R.id.bottom_nav_view)
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.productListsFragment -> {
                    openBottomMenu()
                }
                R.id.recipeListsFragment -> {
                  openBottomMenu()
                }
                R.id.otherFragment -> {
                    openBottomMenu()
                }
                R.id.recipeFragment ->{
                    closeBottomMenuWithHideToolbar()
                }
                else -> {
                   closeBottomMenu()
                }
            }
        }



    }

   private fun openBottomMenu(){
       if (bottomNavView.visibility == View.GONE){
           bottomNavView.visibility = View.VISIBLE
           val animation = AnimationUtils.loadAnimation(this,R.anim.bottom_menu_start)
           bottomNavView.startAnimation(animation)
       }
   }


   private fun closeBottomMenu(){
       val animation = AnimationUtils.loadAnimation(this,R.anim.bottom_menu_clear)
       bottomNavView.startAnimation(animation)
       bottomNavView.visibility = View.GONE
   }


    private fun closeBottomMenuWithHideToolbar(){
        bottomNavView.visibility = View.GONE
    }


    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}