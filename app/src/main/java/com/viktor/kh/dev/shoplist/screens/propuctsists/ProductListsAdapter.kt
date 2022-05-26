package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists


class ProductListsAdapter : RecyclerView.Adapter<ProductListsAdapter.ProductListHolder>() {


    lateinit var data : ArrayList<DataProductLists>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ProductListHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListHolder, position: Int) {
        //holder.bind()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


    fun setData(list: List<DataProductLists>){
        data = ArrayList(list)
    }

    fun addList(list :DataProductLists){
        data.add(list)
    }








    //holder
    class ProductListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListBinding.bind(itemView)

        fun bind (data : DataProductLists) = with(binding){

        }
    } //holder
}