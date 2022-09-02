package com.viktor.kh.dev.shoplist.screens.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe

class FromRecipeAdapter(val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<FromRecipeAdapter.FromRecipeHolder>() {

   var listRecipes = mutableListOf<DataRecipe>()
    var listClicked = mutableListOf<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FromRecipeHolder {
       var view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item_rv,parent,false)
      if (viewType==1){
          view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item_rv_clicked,parent,false)
      }

        return FromRecipeHolder(view)
    }

    override fun onBindViewHolder(holder: FromRecipeHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listRecipes.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listClicked[position]){
            1
        }else{
            0
        }
    }


    fun init(_listRecipes: List<DataRecipe>,_listClicked: List<Boolean>){
        listRecipes.addAll(_listRecipes)
        listClicked.addAll(_listClicked)
    }

    fun clickPosition(position: Int){
        listClicked[position] = !listClicked[position]
        notifyDataSetChanged()
    }



    inner class FromRecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int){
            val text = itemView.findViewById<TextView>(R.id.text_recipe_in_dialog)
            text.text = listRecipes[layoutPosition].name
            itemView.setOnClickListener(View.OnClickListener {
                onItemClickListener.onItemClick(layoutPosition)
            })
        }

    }


    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}