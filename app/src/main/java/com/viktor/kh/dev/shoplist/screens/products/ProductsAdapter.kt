package com.viktor.kh.dev.shoplist.screens.products

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.addProduct
import com.viktor.kh.dev.shoplist.utils.changeReady
import com.viktor.kh.dev.shoplist.utils.deleteProduct
import com.viktor.kh.dev.shoplist.utils.true1
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import kotlin.collections.ArrayList

class ProductsAdapter(
    val onProductClickListener: OnProductClickListener,
    val onProductLongClickListener: OnProductLongClickListener
) : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {

    var data : ArrayList<DataProduct> = ArrayList()
    private var positionClick = 0


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



    fun setData(list: List<DataProduct>, stateForAnim: Int){
        //Log.d("fixLog", "state  = $stateForAnim")
        var animPosition = 0
        var newPosition = 0

            when(stateForAnim){
                addProduct -> {
                    for (i in list.indices){
                            if (!data.contains(list[i])){
                                animPosition = i
                            }
                        }
                    data.clear()
                    data.addAll(list)
                    notifyItemInserted(animPosition)
                }
                changeReady -> {
                    animPosition = positionClick
                   for ( i in list.indices){
                        if (compareProduct(list[i],data[animPosition])){
                           newPosition = i
                        }

                   }
                    data.clear()
                    data.addAll(list)
                    if (animPosition==newPosition){
                        notifyDataSetChanged()
                    }else{
                        notifyItemMoved(animPosition,newPosition)

                    }

                    //Log.d("fixLog", "moved from $animPosition(${data[animPosition].name}) to $newPosition(${data[newPosition].name}) ")
                }
                deleteProduct -> {
                    for (i in data.indices){
                        if (!list.contains(data[i])){
                           animPosition = i
                        }
                    }
                    data.clear()
                    data.addAll(list)
                   notifyItemRemoved(animPosition)
                }

                else -> {
                    data.clear()
                    data.addAll(list)
                    notifyDataSetChanged()
                }

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
                positionClick = layoutPosition
              onProductClickListener.onProductClick(layoutPosition)
            })

            itemView.setOnLongClickListener(View.OnLongClickListener {
                onProductLongClickListener.onProductLongClick(layoutPosition)
                return@OnLongClickListener true
            })

        }



    }
    private fun compareProduct(product1:DataProduct,product2: DataProduct):Boolean{
        return (product1.name==product2.name
                &&product1.date == product2.date
                /*&&product1.ready == product2.ready*/)

    }

    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}