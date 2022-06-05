package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.ItemListBinding
import com.viktor.kh.dev.shoplist.helpers.convertLongToTime
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList


class ProductListsAdapter
constructor(_onListClickListener: OnListClickListener,
            _onSetClickListener: OnSetClickListener,
            _onDelClickListener: OnDelClickListener )
    : RecyclerView.Adapter<ProductListsAdapter.ProductListHolder>() {

    val onListClickListener : OnListClickListener
    val onSetClickListener : OnSetClickListener
    val onDelClickListener: OnDelClickListener

    lateinit var data : ArrayList<DataProductList>
init {
    onDelClickListener = _onDelClickListener
    onListClickListener = _onListClickListener
    onSetClickListener = _onSetClickListener

}
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
        data = ArrayList(list)
    }

    fun addList(list :DataProductList){
        data.add(list)
    }

    fun deleteList(list : DataProductList){

    }




    //holder
   inner class ProductListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemListBinding.bind(itemView)

        fun bind (data : DataProductList) = with(binding){
           listName.text = data.name
            textListDate.text = data.date?.let { convertLongToTime(it) }
            textListReady.text = data.products?.let { findReady(it) }
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



        private fun initClick(){

        }



       private fun findReady(list: List<DataProduct>): String{
           var containsReady = 0
           for(i in list){
               if(i.ready == true){
                   containsReady++
               }
           }
           return "$containsReady/${list.size}"
       }
    } //holder


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