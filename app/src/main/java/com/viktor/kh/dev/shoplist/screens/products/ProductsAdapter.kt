package com.viktor.kh.dev.shoplist.screens.products

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

   fun setData(list: List<DataProduct>){

   }

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }
}