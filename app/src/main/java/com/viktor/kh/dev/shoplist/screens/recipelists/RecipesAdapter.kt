package com.viktor.kh.dev.shoplist.screens.recipelists

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.utils.*

class RecipesAdapter constructor(val onListClickListener: OnListClickListener,
                                 val onSetClickListener: OnSetClickListener,
                                 val onDelClickListener: OnDelClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeHolder>() {



    var data : ArrayList<DataRecipe> = ArrayList()
    var context: Context? = null
    var nightTheme: Boolean = false
    var deletePosition = 0
    var isSearch = false

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
            currentCardColor = cardColor(data.id)
            Log.d("fixLog", "card color in recipe = $currentCardColor")
            if (currentCardColor!=0){
                cl.setCardBackgroundColor(cardColor(data.id))
            }else{
                if (nightTheme){
                    cl.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
                }else{
                    cl.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimaryDay))
                }
            }
            listName.text = data.name
            val date  = data.date?.let { convertLongToTime(it) }.toString()
            textListDate.text = date
            textListReady.visibility = View.GONE
            if (nightTheme&&currentCardColor==0){
                listName.setTextColor(Color.WHITE)
                textListDate.setTextColor(Color.WHITE)
                editImage.setImageResource(R.drawable.ic_baseline_edit_white_24)
                deleteImage.setImageResource(R.drawable.ic_baseline_delete_white_24)
            }else{
                listName.setTextColor(Color.BLACK)
                textListDate.setTextColor(Color.BLACK)
                editImage.setImageResource(R.drawable.ic_edit_black_24dp)
                deleteImage.setImageResource(R.drawable.ic_delete_forever_black_24dp)

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


    }

    private fun cardColor(color :Int?):Int{
        return try {
            if (color!=null){
                ContextCompat.getColor(context!!, color)
            }else{
                0
            }
        }catch (e : Resources.NotFoundException){
            writeLog("error color selected = ${e.printStackTrace()}",
                context!!.applicationContext,true)
            0
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