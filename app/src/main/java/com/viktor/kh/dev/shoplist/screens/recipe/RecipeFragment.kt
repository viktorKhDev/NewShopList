package com.viktor.kh.dev.shoplist.screens.recipe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.RecipeFragmentBinding
import com.viktor.kh.dev.shoplist.utils.listName
import com.viktor.kh.dev.shoplist.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_fragment) {


   private lateinit var binding : RecipeFragmentBinding


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipeFragmentBinding.bind(view)
        initActionbar()
       initClicks()

    }



    private fun initClicks() = with(binding){

          productsFab.setOnClickListener(View.OnClickListener {
            recipeProductList.visibility = View.VISIBLE
            val alphaColor = Color.argb(50,0,0,0)
            recipeProductList.setBackgroundColor(alphaColor)
            val anim = AnimationUtils.loadAnimation(context,R.anim.scale_show_center)
              rv.startAnimation(anim)
              rv.focusable = View.FOCUSABLE
              scrollText.isNestedScrollingEnabled = false

          })


        blackoutFrameImgTop.setOnClickListener(View.OnClickListener {
            recipeProductList.visibility = View.GONE
            scrollText.isNestedScrollingEnabled = true

        })
        blackoutFrameImgBottom.setOnClickListener(View.OnClickListener {
            recipeProductList.visibility = View.GONE
            scrollText.isNestedScrollingEnabled = true

        })

    }


    private fun initActionbar() = with(binding){
        val supportActionBar: androidx.appcompat.app.ActionBar
                = (activity as AppCompatActivity).supportActionBar!!
        supportActionBar.hide()
        val listName = arguments?.getString(listName)
        collapsingToolbar.title = listName
        toolbar.inflateMenu(R.menu.options_menu_in_recipe)
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

               R.id.home -> activity!!.onBackPressed()

                R.id.share_item -> showToast("share in recipe",context)
            }
            false
        }

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            activity!!.onBackPressed()
        })

    }


    private fun stopScrollAppbar() = with(binding){

    }

    override fun onStop() {

        super.onStop()
    }
}