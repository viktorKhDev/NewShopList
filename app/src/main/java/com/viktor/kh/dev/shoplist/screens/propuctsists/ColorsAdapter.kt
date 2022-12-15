package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.SetColorItemBinding
import com.viktor.kh.dev.shoplist.utils.currentCardColor
import com.viktor.kh.dev.shoplist.utils.getColors


class ColorsAdapter(val context: Context): RecyclerView.Adapter<ColorsAdapter.ColorHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.set_color_item,parent,false)
        return ColorHolder(view)
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
       holder.bind(getColors()[position],context)
    }

    override fun getItemCount(): Int {
        return getColors().size
    }





    class ColorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = SetColorItemBinding.bind(itemView)


        fun bind(color : Int,context: Context) = with(binding){
            Log.d("fix", "color in circle = $color" )

            colorCircle.setColorFilter(ContextCompat.getColor(context, color));
            colorStroke.setColorFilter(ContextCompat.getColor(context,R.color.black))

        // colorCircle.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))

        }

    }

}