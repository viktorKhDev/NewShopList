package com.viktor.kh.dev.shoplist.utils

class ColorClickedList {

    private val colorsSwitch = mutableMapOf<Int,Boolean>()

    init {
        for (color in getColors()){
            colorsSwitch[color] = false
        }
    }

    fun clickColor(position: Int){
        for (id in getColors().indices){
            colorsSwitch[getColors()[id]] = id==position
        }
    }


   fun isClickedColor(color: Int):Boolean{
      return colorsSwitch[color]!!
   }


    fun getCurrentColor():Int?{
        var currentColor: Int? = null
        for(color in colorsSwitch){
            if (color.value) currentColor = color.key
        }
        return currentColor
    }

    fun getCurrentColorPosition():Int{
        var currentColorPosition = 0
        for(color in colorsSwitch){
            currentColorPosition++
            if (color.value) break
        }
        return currentColorPosition
    }
}