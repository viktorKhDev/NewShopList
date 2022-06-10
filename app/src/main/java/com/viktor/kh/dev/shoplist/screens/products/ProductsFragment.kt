package com.viktor.kh.dev.shoplist.screens.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_add) {

    private val model: ProductsModel by activityViewModels()
    private lateinit var binding: FragmentAddBinding
    private lateinit var listName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
       val listId = arguments?.getInt("listID")
    }

}