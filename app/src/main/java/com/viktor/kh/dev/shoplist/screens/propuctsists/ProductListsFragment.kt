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
class ProductListsFragment: Fragment(R.layout.fragment_lists)
{

    private val model: ProductListsModel by activityViewModels()
     private lateinit var binding : FragmentListsBinding
     private lateinit var list: RecyclerView
     lateinit var adapter: ProductListsAdapter

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListsBinding.bind(view)
        list = binding.includeLists.lists
         model.dataLists.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
       })

    }


    fun subscribeData(data: List<DataProductLists>){
      adapter.setData(data)
    }

   fun initList(){
       val onListClickListener = object : ProductListsAdapter.OnListClickListener {
           override fun onListClick() {
               //click on item
           }
       }

       val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
           override fun onDelClick() {
               // click on del button
           }
       }
       val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
           override fun onSet() {
               // click on det button
           }


       }
       list.adapter = ProductListsAdapter(onListClickListener, onSetClickListener, onDelClickListener)

   }




}