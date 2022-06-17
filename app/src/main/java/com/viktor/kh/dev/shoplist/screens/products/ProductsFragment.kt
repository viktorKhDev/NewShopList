package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentAddBinding
import com.viktor.kh.dev.shoplist.helpers.currentTimeToLong
import com.viktor.kh.dev.shoplist.helpers.falce0
import com.viktor.kh.dev.shoplist.helpers.showToast
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_add) {

    private val model: ProductsModel by activityViewModels()
    private lateinit var binding: FragmentAddBinding
    private lateinit var rv: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        rv  = binding.listProducts
        val  listId = arguments?.getInt("listID")!!
        model.init(listId)
        binding.addProduct.setOnClickListener(View.OnClickListener {
            addProduct()
        })
        initRv()

        model.productsList.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
        })


    }

    private fun addProduct() = with(binding) {
       relativeAddProduct.visibility = View.VISIBLE
        addProduct.visibility = View.GONE
        btnAcceptProduct.setOnClickListener(View.OnClickListener {
            val productName : String = textProduct.text.toString()
            if(productName.isNotEmpty()){
                val dataProduct = DataProduct(productName, currentTimeToLong().toString(), falce0)
                textProduct.setText("")
                model.addProduct(dataProduct)
            }else{
                showToast(getString(R.string.input_the_title),context)
            }

        })
        btnNoProduct.setOnClickListener(View.OnClickListener {
            textProduct.text.clear()
            relativeAddProduct.visibility = View.GONE
            addProduct.visibility = View.VISIBLE
        })

    }

    private fun setProduct(){

    }

    private fun subscribeData(data :List<DataProduct>){
        productsAdapter.setData(data)
    }

    private fun initRv(){
        //init recyclerView

        val onClickListener = object : ProductsAdapter.OnProductClickListener {
            override fun onProductClick(position: Int) {
                model.onItemClick(position)
            }

        }

        val onLongClickListener = object: ProductsAdapter.OnProductLongClickListener{
            override fun onoProductLongClick(position: Int) {
                model.onItemLongClick(position)
            }

        }

        productsAdapter = ProductsAdapter(onClickListener,onLongClickListener)

        rv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
        }

      Log.d("MyLog", "rv init")
    }

}