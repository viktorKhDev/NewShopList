package com.viktor.kh.dev.shoplist.repository.db.data



import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter


@TypeConverters(ProductsConverter::class)
data class DataProduct(
        @SerializedName("name") val name: String?,
        @SerializedName("date") val date: Long?,
        @SerializedName("ready") val ready : Boolean?
)
