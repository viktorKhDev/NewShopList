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
import com.viktor.kh.dev.shoplist.databinding.SupportFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment: Fragment(R.layout.support_fragment) {



   private var binding :SupportFragmentBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SupportFragmentBinding.bind(view)
        initActionbar()

    }




    private fun initActionbar() = with(binding!!){
        toolbar.apply {
            title = getString(R.string.support)
            setNavigationOnClickListener(View.OnClickListener {
                requireActivity().onBackPressed()
            })
        }

    }



}