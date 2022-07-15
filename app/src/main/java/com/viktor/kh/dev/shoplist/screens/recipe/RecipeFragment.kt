package com.viktor.kh.dev.shoplist.screens.recipe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.RecipeFragmentBinding
import com.viktor.kh.dev.shoplist.databinding.RecipesFragmentBinding
import com.viktor.kh.dev.shoplist.utils.listName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_fragment) {


   private lateinit var binding : RecipeFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipeFragmentBinding.bind(view)
      //  initActionbar()
    }



    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        val listName = arguments?.getString(listName)
        supportActionBar.apply {
            this!!.title = listName
            setDisplayHomeAsUpEnabled(true)
            setShowHideAnimationEnabled(false)

        }

    }

    override fun onStop() {
        super.onStop()

    }
}