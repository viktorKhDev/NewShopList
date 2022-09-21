package com.viktor.kh.dev.shoplist.screens.recipelists

import android.content.Context
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
import com.viktor.kh.dev.shoplist.utils.colorLists
import com.viktor.kh.dev.shoplist.utils.colors
import com.viktor.kh.dev.shoplist.utils.convertLongToTime
import com.viktor.kh.dev.shoplist.utils.currentCardColor
import kotlin.random.Random

class RecipesAdapter constructor(val onListClickListener: OnListClickListener,
                                 val onSetClickListener: OnSetClickListener,
                                 val onDelClickListener: OnDelClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeHolder>() {



    var data : ArrayList<DataRecipe> = ArrayList()
    var context: Context? = null
    var deletePosition = 0
    var isSearch = false
    private var currentItemColor  = Random.nextInt(0, colors.size-1)
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
            itemView.setOnClickListener(View.OnClickListener {
                currentCardColor = cardColor(data.id)
                Log.d("fix","currentCardColor in adapter = ${currentCardColor.toString()}" )
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
                if (currentItemColor==colors.size-1) currentItemColor = 0 else currentItemColor++
                colorMap.put(listID, colors[currentItemColor])
                return ContextCompat.getColor(context!!, colors[currentItemColor])

            }
        }else{
            return  ContextCompat.getColor(context!!, R.color.back_list_item)
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