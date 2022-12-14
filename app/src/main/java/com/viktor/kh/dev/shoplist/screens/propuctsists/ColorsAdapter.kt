package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.getColors


class ColorsAdapter: RecyclerView.Adapter<ColorsAdapter.ColorHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.set_color_item,parent,false)
        return ColorHolder(view)
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
       holder.bind(getColors()[position])
    }

    override fun getItemCount(): Int {
        return getColors().size
    }



    class ColorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(color : Int){

        }

    }

}