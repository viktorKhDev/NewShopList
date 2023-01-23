package com.viktor.kh.dev.shoplist.screens.other.main




import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.isNightTheme
import com.viktor.kh.dev.shoplist.utils.loadSetting
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView : BottomNavigationView
    private val model: MainModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var rootLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadSetting(this)
        setContentView(R.layout.main_activity)
        rootLayout = findViewById(R.id.main_root_layout)
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavView =  findViewById(R.id.bottom_nav_view)
        bottomNavView.setupWithNavController(navController)
        setColors()
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
                   closeBottomMenuWithoutAnim()
                }
                R.id.productsFragment ->{
                   closeBottomMenu()
                }

                else -> {
                  closeBottomMenu()
                }
            }
        }



    }

   private fun openBottomMenu(){
       setColors()
       if (bottomNavView.visibility == View.GONE){
           bottomNavView.visibility = View.VISIBLE
           val animation = AnimationUtils.loadAnimation(this,R.anim.bottom_menu_start)
           animation.startOffset = 200
           bottomNavView.startAnimation(animation)
       }
   }


    private fun closeBottomMenu(){
        val animation = AnimationUtils.loadAnimation(this,R.anim.bottom_menu_clear)
        animation.startOffset = 200
        bottomNavView.startAnimation(animation)
        bottomNavView.visibility = View.GONE
    }


    private fun closeBottomMenuWithoutAnim(){
        bottomNavView.visibility = View.GONE
    }

    private fun openBottomMenuWithoutAnim(){
        bottomNavView.visibility = View.VISIBLE
    }




    private fun setColors(){
        if (isNightTheme(this)){
            bottomNavView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
            rootLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
            bottomNavView.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(this,R.drawable.bottom_nav_views_item_color))
        }else{
            bottomNavView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDay))
            rootLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDay))
            bottomNavView.itemTextColor = ColorStateList.valueOf(ContextCompat.getColor(this,R.drawable.bottom_nav_views_item_color))
        }

    }


}