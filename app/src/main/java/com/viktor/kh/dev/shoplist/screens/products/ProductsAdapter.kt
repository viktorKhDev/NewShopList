package com.viktor.kh.dev.shoplist.screens.products

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.helpers.addProduct
import com.viktor.kh.dev.shoplist.helpers.changeReady
import com.viktor.kh.dev.shoplist.helpers.deleteProduct
import com.viktor.kh.dev.shoplist.helpers.true1
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import kotlin.collections.ArrayList

class ProductsAdapter(
    val onProductClickListener: OnProductClickListener,
    val onProductLongClickListener: OnProductLongClickListener
) : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {

    var data : ArrayList<DataProduct> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        if (viewType==1) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_ready,parent,false)
        }
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(data[position].ready == true1){
            1
        }else{
            0
        }
    }

    fun setData(list: List<DataProduct>,stateForAnim: Int,position: Int
    ,newPosition: Int){

        data.clear()
        data.addAll(list)
        if (position!=-1){
            when(stateForAnim){
                addProduct -> {
                    notifyItemInserted(position)
                }

                deleteProduct -> {
                    notifyItemRemoved(position)
                }

                changeReady -> {
                   notifyItemMoved(position,newPosition)
                }

                else -> {
                    notifyDataSetChanged()
                }
            }
        }else{
            notifyDataSetChanged()
        }
    }



    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(product: DataProduct){
           var text = itemView.findViewById<TextView>(R.id.productName)
            if (itemViewType == 1) {
                text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text.text = product.name
            } else {
                text.text = product.name
            }

            itemView.setOnClickListener(View.OnClickListener {
              onProductClickListener.onProductClick(layoutPosition)
            })

            itemView.setOnLongClickListener(View.OnLongClickListener {
                onProductLongClickListener.onProductLongClick(layoutPosition)
                return@OnLongClickListener true
            })

        }



    }

    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}