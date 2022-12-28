package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.SetColorItemBinding
import com.viktor.kh.dev.shoplist.utils.ColorClickedList
import com.viktor.kh.dev.shoplist.utils.getColors


class ColorsAdapter(val context: Context,
val onColorClick: OnColorClickListener): RecyclerView.Adapter<ColorsAdapter.ColorHolder>() {

     var data = ColorClickedList()

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





    inner class ColorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = SetColorItemBinding.bind(itemView)


        fun bind(color : Int) = with(binding){
            Log.d("fix", "color in circle = $color" )
            colorCircle.setColorFilter(ContextCompat.getColor(context, color));
            if (data.isClickedColor(color)){
                colorStroke.setColorFilter(ContextCompat.getColor(context,R.color.black))
            }else{
                colorStroke.setColorFilter(ContextCompat.getColor(context,color))
            }
            colorCircle.setOnClickListener(View.OnClickListener {
                onColorClick.onClick(layoutPosition)
                data.clickColor(layoutPosition)
            })
        // colorCircle.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))

        }

    }


    interface OnColorClickListener{
        fun onClick(position: Int)
    }

}