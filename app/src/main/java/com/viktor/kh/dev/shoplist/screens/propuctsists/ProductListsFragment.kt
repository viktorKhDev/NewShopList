package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentListsBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ProductListsFragment: Fragment(R.layout.fragment_lists) {

    private val model: ProductListsModel by activityViewModels()
     private lateinit var binding : FragmentListsBinding
     private lateinit var list: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListsBinding.bind(view)
        list = binding.includeLists.lists
       model.dataLists.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
       })

    }


    fun subscribeData(data: List<DataProductLists>){

    }


}