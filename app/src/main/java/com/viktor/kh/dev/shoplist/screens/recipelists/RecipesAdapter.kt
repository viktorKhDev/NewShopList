package com.viktor.kh.dev.shoplist.screens.recipelists

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.screens.propuctsists.ProductListsAdapter
import com.viktor.kh.dev.shoplist.utils.convertLongToTime

class RecipesAdapter constructor(val onListClickListener: ProductListsAdapter.OnListClickListener,
                                 val onSetClickListener: ProductListsAdapter.OnSetClickListener,
                                 val onDelClickListener: ProductListsAdapter.OnDelClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeHolder>() {



    var data : ArrayList<DataRecipe> = ArrayList()

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
        data = ArrayList(list)
        Log.d("MyLog" , "${data.size} in adapter")
        notifyDataSetChanged()

    }

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemListBinding.bind(itemView)
        fun bind(data : DataRecipe) = with(binding){
            listName.text = data.name
            val date  = data.date?.let { convertLongToTime(it) }.toString()
            Log.d("MyLog", "listDate = $date")
            textListDate.text = date
            textListReady.visibility = View.GONE
            itemView.setOnClickListener(View.OnClickListener {
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