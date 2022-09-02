package com.viktor.kh.dev.shoplist.screens.recipe

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.utils.ADD_PRODUCT
import com.viktor.kh.dev.shoplist.utils.DELETE_PRODUCT


class RecipeProductsAdapter(
    val onProductClickListener: RecipeProductsAdapter.OnProductClickListener,
    val onProductLongClickListener: RecipeProductsAdapter.OnProductLongClickListener
): RecyclerView.Adapter<RecipeProductsAdapter.ProductHolder>() {



    var data : ArrayList<DataProduct> = ArrayList()
    private var positionClick = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return  data.size
    }


    fun setData(list: List<DataProduct>, stateForAnim: Int){
        var animPosition = 0

        when(stateForAnim){
            ADD_PRODUCT -> {
                for (i in list.indices){
                    if (!data.contains(list[i])){
                        animPosition = i
                    }
                }
                data.clear()
                data.addAll(list)
                notifyItemInserted(animPosition)
            }
            DELETE_PRODUCT -> {
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
                text.text = "${product.name} ${product.amount}"
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


    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}