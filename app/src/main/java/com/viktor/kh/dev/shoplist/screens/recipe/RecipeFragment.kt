package com.viktor.kh.dev.shoplist.screens.recipe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.listName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_tab_layout_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
    }



    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        val listName = arguments?.getString(listName)
        supportActionBar?.title = listName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}