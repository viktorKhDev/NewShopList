package com.viktor.kh.dev.shoplist.utils



class ColorClickedList {

    private val colorsSwitch = mutableMapOf<Int,Boolean>()

    init {
        for (color in getColors()){
            colorsSwitch[color] = false
        }
    }

    fun clickColor(position: Int){
        val currentPosition = getCurrentColorPosition()
        for (id in getColors().indices){
            colorsSwitch[getColors()[id]] = false
        }

        if (position!=currentPosition-1){
            colorsSwitch[getColors()[position]] = true
        }
    }


    fun clickColorWithColor(color: Int){
        for (id in getColors().indices){
            colorsSwitch[getColors()[id]] = false
        }
        colorsSwitch[color] = true
    }


    fun clearClick(){
        for (id in getColors().indices){
            colorsSwitch[getColors()[id]] = false
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
        var count = 0
        for(color in colorsSwitch){
            count++
            if (color.value){
                currentColorPosition = count
                break
            }

        }
        return currentColorPosition
    }



}