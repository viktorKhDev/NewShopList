package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.viktor.kh.dev.shoplist.R
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ProductListsFragment: Fragment(R.layout.activity_lists) {

    private val model: ProductListsModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}