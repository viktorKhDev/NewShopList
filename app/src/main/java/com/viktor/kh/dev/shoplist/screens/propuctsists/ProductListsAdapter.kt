package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.helpers.convertLongToTime
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList


class ProductListsAdapter
constructor(val onListClickListener: OnListClickListener,
            val onSetClickListener: OnSetClickListener,
            val onDelClickListener: OnDelClickListener )
    : RecyclerView.Adapter<ProductListsAdapter.ProductListHolder>() {

    lateinit var data : ArrayList<DataProductList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        Log.d("MyLog" , "onCreateViewHolder in adapter")
        return ProductListHolder(view)

    }

    override fun onBindViewHolder(holder: ProductListHolder, position: Int) {
        Log.d("MyLog" , " onBindViewHolder in adapter")
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun setData(list: List<DataProductList>){
        data = ArrayList(list)
        Log.d("MyLog" , "${data.size} in adapter")
        notifyDataSetChanged()

    }


    //holder
   inner class ProductListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBinding.bind(itemView)

        fun bind (data : DataProductList) = with(binding){
            Log.d("MyLog", "${data.name} in holder")
           listName.text = data.name
            textListDate.text = convertLongToTime(data.date)
            textListReady.text = data.products?.let { findReady(it) }
            itemView.setOnClickListener(View.OnClickListener {
                onListClickListener.onListClick(layoutPosition)
            })
            editImage.setOnClickListener(View.OnClickListener {
                onSetClickListener.onSet(layoutPosition)
            })
            deleteImage.setOnClickListener(View.OnClickListener {
                onDelClickListener.onDelClick(layoutPosition)
            })
        }




        private fun findReady(list: List<DataProduct>): String{
           var containsReady = 0
           for(i in list){
               if(i.ready == true){
                   containsReady++
               }
           }
           return "$containsReady/${list?.size ?: 0}"
       }
    } //holder


    interface  OnDelClickListener{
        fun onDelClick(position: Int)
    }

    interface OnListClickListener{
        fun onListClick(position: Int)
    }

    interface OnSetClickListener{
        fun onSet(position: Int)
    }

}