package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.R

class ProductListsFragment: Fragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lists, container,false)

    }
}