package com.viktor.kh.dev.shoplist.screens.recipelists

import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListsFragment : Fragment(R.layout.recipes_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
    }


    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = getString(R.string.recipes)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}