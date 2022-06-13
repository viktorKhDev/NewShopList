package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentAddBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_add) {

    private lateinit var model: ProductsModel
    private lateinit var binding: FragmentAddBinding
    private lateinit var rv: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        rv  = binding.listProducts
        initRv()
      val  listId = arguments?.getInt("listID")?.toInt()
        model = ViewModelProvider(this,ProductsModelFactory(activity!!.application, listId!!))
                .get(ProductsModel::class.java)


        model.productsList.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
        })

        binding.addProduct.setOnClickListener(View.OnClickListener {
            addProduct()
        })


    }

    private fun addProduct(){

    }

    private fun setProduct(){

    }

    private fun subscribeData(data :List<DataProduct>){
        productsAdapter.setData(data)
    }

    private fun initRv(){
        //init recyclerView

    }

}