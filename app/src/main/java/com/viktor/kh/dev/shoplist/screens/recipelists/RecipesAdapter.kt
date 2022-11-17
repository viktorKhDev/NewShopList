package com.viktor.kh.dev.shoplist.screens.recipelists

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.screens.propuctsists.ProductListsAdapter
import com.viktor.kh.dev.shoplist.utils.*
import kotlin.random.Random

class RecipesAdapter constructor(val onListClickListener: OnListClickListener,
                                 val onSetClickListener: OnSetClickListener,
                                 val onDelClickListener: OnDelClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeHolder>() {



    var data : ArrayList<DataRecipe> = ArrayList()
    var context: Context? = null
    var nightTheme: Boolean = false
    var deletePosition = 0
    var isSearch = false
    private var currentItemColor  = ColorId.get()
    private var colorMap = mutableMapOf<Int,Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        Log.d("MyLog" , "onCreateViewHolder in adapter")
        return RecipeHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
       holder.bind(data[position])
    }

    override fun getItemCount(): Int {
       return data.size
    }

    fun setData(list: List<DataRecipe>){
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

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemListBinding.bind(itemView)
        fun bind(data : DataRecipe) = with(binding){
            cl.setCardBackgroundColor(cardColor(data.id))
            listName.text = data.name
            val date  = data.date?.let { convertLongToTime(it) }.toString()
            textListDate.text = date
            textListReady.visibility = View.GONE
            if (nightTheme&&!colorLists){
                listName.setTextColor(Color.WHITE)
                textListDate.setTextColor(Color.WHITE)
                editImage.setImageResource(R.drawable.ic_baseline_edit_white_24)
                deleteImage.setImageResource(R.drawable.ic_baseline_delete_white_24)
            }
            itemView.setOnClickListener(View.OnClickListener {
                currentCardColor = cardColor(data.id)
                onListClickListener.onListClick(layoutPosition)
            })
            editImage.setOnClickListener(View.OnClickListener {
                onSetClickListener.onSet(layoutPosition)
            })
            deleteImage.setOnClickListener(View.OnClickListener {
                onDelClickListener.onDelClick(layoutPosition)
            })
        }


    }

    private fun cardColor(listID :Int):Int{
        if (colorLists){
            if (colorMap.contains(listID)){
                return ContextCompat.getColor(context!!, colorMap[listID]!!)
            }else{
                if (currentItemColor==getColors(context!!).size-1) currentItemColor = 0 else currentItemColor++
                colorMap.put(listID, getColors(context!!)[currentItemColor])
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