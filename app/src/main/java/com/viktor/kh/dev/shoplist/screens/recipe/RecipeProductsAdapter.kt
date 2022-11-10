package com.viktor.kh.dev.shoplist.screens.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.utils.*
import kotlin.random.Random


class RecipeProductsAdapter(
    val onProductClickListener: RecipeProductsAdapter.OnProductClickListener,
    val onProductLongClickListener: RecipeProductsAdapter.OnProductLongClickListener
): RecyclerView.Adapter<RecipeProductsAdapter.ProductHolder>() {



    var data : ArrayList<DataProduct> = ArrayList()
    private var positionClick = 0
    private var currentItemColor  = ColorId.get()
    private var colorMap = mutableMapOf<DataProduct,Int>()
    var context: Context? = null
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
            var card = itemView.findViewById<CardView>(R.id.cl)
            card.setCardBackgroundColor(cardColor(product))
                text.text = "${product.name} ${product.amount}"

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



    private fun cardColor(product: DataProduct):Int{
        if (colorItems){
            if (colorMap.contains(product)){
                return ContextCompat.getColor(context!!, colorMap[product]!!)
            }else{
                if (currentItemColor==getColors(context!!).size-1) currentItemColor = 0 else currentItemColor++
                colorMap.put(product, getColors(context!!)[currentItemColor])
                return ContextCompat.getColor(context!!, getColors(context!!)[currentItemColor])

            }
        }else{
            if (isNightTheme(context!!)){
                return  ContextCompat.getColor(context!!, R.color.colorPrimary)
            }else{
                return  ContextCompat.getColor(context!!, R.color.colorPrimaryDay)
            }
        }


    }

    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}