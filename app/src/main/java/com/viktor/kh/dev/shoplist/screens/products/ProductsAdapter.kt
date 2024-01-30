package com.viktor.kh.dev.shoplist.screens.products

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.utils.*

import kotlin.collections.ArrayList

class ProductsAdapter(
    val onProductClickListener: OnProductClickListener,
    val onProductLongClickListener: OnProductLongClickListener
) : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {

    var data : ArrayList<ProductData> = ArrayList()
    private var positionClick = 0
    var nightTheme: Boolean = false
    var context: Context? = null

    private var currentItemColor  = ColorId.get()
    private var colorMap = mutableMapOf<ProductData,Int>()

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
        return if(data[position].ready == true){
            1
        }else{
            0
        }
    }



    fun setData(list: List<ProductData>, stateForAnim: Int){
        Log.d("fix", "colorItems = $colorItems")

        var animPosition = 0
          var newPosition = 0

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
                CHANGE_READY -> {
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

        fun bind(product: ProductData){

           var text = itemView.findViewById<TextView>(R.id.productName)
            if (itemViewType == 1) {
                text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text.text = "${product.name} ${product.amount}"
                if (nightTheme){
                    text.setTextColor(Color.LTGRAY)
                }else{
                    text.setTextColor(Color.GRAY)
                }

            } else {

               val color = cardColor(product)
                val card = itemView.findViewById<CardView>(R.id.cl)
                if (colorItems){
                    card.setCardBackgroundColor(color)
                    text.setTextColor(Color.BLACK)
                }else{

                    if (nightTheme){
                        card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
                        text.setTextColor(Color.WHITE)
                    }else{
                        card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimaryDay))
                        text.setTextColor(Color.BLACK)
                    }
                }
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
    private fun compareProduct(product1:ProductData,product2: ProductData):Boolean{
        return (product1.name==product2.name
                &&product1.date == product2.date
                )

    }


  /*  private fun cardColor(color: Int?):Int?{
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


    }*/

    private fun cardColor(product: ProductData):Int{
        if (colorMap.contains(product)){
            return ContextCompat.getColor(context!!, colorMap[product]!!)
        }else{
            if (currentItemColor== getColors().size-1) currentItemColor = 0 else currentItemColor++
            colorMap.put(product, getColors()[currentItemColor])
            return ContextCompat.getColor(context!!, getColors()[currentItemColor])

        }


    }

    interface  OnProductClickListener{
        fun onProductClick(position: Int)
    }
    interface OnProductLongClickListener{
        fun onProductLongClick(position: Int)
    }
}