package com.viktor.kh.dev.shoplist.repository.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct


object ProductsConverter {

    val gson = Gson()

        @TypeConverter
        fun fromProductList( productList: List<DataProduct>?): String?{
           return if(productList == null) null else gson.toJson(productList)
        }



        @TypeConverter
        fun toProductList(s: String?): List<DataProduct>?{
            val myType = object : TypeToken<List<DataProduct?>>() {}.type
            return if(s == null) null else gson.fromJson(s, myType)

        }



}

