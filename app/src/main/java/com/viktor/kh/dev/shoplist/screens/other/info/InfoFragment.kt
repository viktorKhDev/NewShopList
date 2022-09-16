package com.viktor.kh.dev.shoplist.screens.other.info

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.viktor.kh.dev.shoplist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment: Fragment(R.layout.support_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
        initMenu()
    }




    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = getString(R.string.support)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun initMenu(){

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    android.R.id.home -> {activity!!.onBackPressed()
                        true}
                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

}