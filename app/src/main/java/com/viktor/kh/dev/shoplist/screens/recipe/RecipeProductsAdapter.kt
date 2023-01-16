package com.viktor.kh.dev.shoplist.screens.recipe

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
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


class RecipeProductsAdapter(
    val onProductClickListener: RecipeProductsAdapter.OnProductClickListener,
    val onProductLongClickListener: RecipeProductsAdapter.OnProductLongClickListener
): RecyclerView.Adapter<RecipeProductsAdapter.ProductHolder>() {



    var data : ArrayList<DataProduct> = ArrayList()
    private var positionClick = 0
    private var currentItemColor  = ColorId.get()
    private var colorMap = mutableMapOf<DataProduct,Int>()
    var context: Context? = null
    var nightTheme: Boolean = false

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
            val color = cardColor(product.color)
            val card = itemView.findViewById<CardView>(R.id.cl)
            if (color!=null){
                card.setCardBackgroundColor(color)
            }else{
                if (nightTheme){
                    card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
                }else{
                    card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimaryDay))
                }
            }


            text.text = "${product.name} ${product.amount}"
            if (nightTheme&&!colorItems){
                text.setTextColor(Color.WHITE)
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



    private fun cardColor(color: Int?):Int?{
        return try {
            if (color!=null){
                ContextCompat.getColor(context!!, color)
            }else{
                null
            }
        }catch (e : Resources.NotFoundException){
            writeLog("error color selected = ${e.printStackTrace()}",
                context!!.applicationContext,true)
            null
        }

    }

    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}