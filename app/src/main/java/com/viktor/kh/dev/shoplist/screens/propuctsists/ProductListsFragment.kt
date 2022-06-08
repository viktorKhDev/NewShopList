package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.DialogAddBinding
import com.viktor.kh.dev.shoplist.databinding.FragmentListsBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
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
         initList()
         binding.fabAddList.setOnClickListener(View.OnClickListener {
             addList()
         })
         model.dataLists.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
       })

    }
    private fun subscribeData(data: List<DataProductList>){
      adapter.setData(data)
    }

   private fun initList(){
       val onListClickListener = object : ProductListsAdapter.OnListClickListener {
           override fun onListClick(position: Int) {
               //click on item
           }
       }

       val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
           override fun onDelClick(position: Int) {
               // click on del button
           }
       }
       val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
           override fun onSet(position: Int) {
               // click on det button
           }


       }
       adapter = ProductListsAdapter(onListClickListener, onSetClickListener, onDelClickListener)
       list.adapter = adapter


   }

    private fun addList(){
        AddDialog(model).showsDialog
    }

}