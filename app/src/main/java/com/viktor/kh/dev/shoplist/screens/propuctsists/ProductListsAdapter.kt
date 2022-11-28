package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.utils.*
import kotlin.random.Random


class ProductListsAdapter
constructor(val onListClickListener: OnListClickListener,
            val onSetClickListener: OnSetClickListener,
            val onDelClickListener: OnDelClickListener )
    : RecyclerView.Adapter<ProductListsAdapter.ProductListHolder>() {


    var context: Context? = null
    var nightTheme: Boolean = false
    private var currentItemColor  = ColorId.get()
    var data : ArrayList<DataProductList> = ArrayList()
    var deletePosition = 0
    private var colorMap = mutableMapOf<Int,Int>()
    var isSearch = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ProductListHolder(view)


    }

    override fun onBindViewHolder(holder: ProductListHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun setData(list: List<DataProductList>){
        if (isSearch){
            data = ArrayList(list)
            notifyDataSetChanged()
        }else{
            if (list.size>data.size){
                data = ArrayList(list)
                notifyItemInserted(list.size-1)

            }else if(list.size<data.size){
                notifyItemRemoved(deletePosition)
                data = ArrayList(list)
            }else{
                data = ArrayList(list)
                notifyDataSetChanged()
            }
        }
       isSearch = false

    }

    //holder
   inner class ProductListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListBinding.bind(itemView)

        fun bind (data : DataProductList) = with(binding){
            if (colorLists){
                cl.setCardBackgroundColor(cardColor(data.id))
                currentCardColor = cardColor(data.id)
            }
            listName.text = data.name
            val date  = data.date?.let { convertLongToTime(it) }.toString()
            textListDate.text = date
            textListReady.text = data.products?.let { findReady(it) }
            if (nightTheme&&!colorLists){
                listName.setTextColor(Color.WHITE)
                textListDate.setTextColor(Color.WHITE)
                textListReady.setTextColor(Color.WHITE)
                editImage.setImageResource(R.drawable.ic_baseline_edit_white_24)
                deleteImage.setImageResource(R.drawable.ic_baseline_delete_white_24)
            }
            itemView.setOnClickListener(View.OnClickListener {
                if (colorLists){
                    currentCardColor = cardColor(data.id)
                }
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
            val s  = "$containsReady/${list?.size ?: 0}"
            return s
       }
    }


   private fun cardColor(listID:Int):Int{

       if (colorMap.contains(listID)){
               return ContextCompat.getColor(context!!, colorMap[listID]!!)
           }else{
               if (currentItemColor==getColors(context!!).size-1) currentItemColor = 0 else currentItemColor++
               colorMap.put(listID, getColors(context!!)[currentItemColor])
               return ContextCompat.getColor(context!!, getColors(context!!)[currentItemColor])

           }

   }



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