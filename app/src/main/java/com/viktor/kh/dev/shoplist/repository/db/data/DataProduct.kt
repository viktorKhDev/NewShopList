package com.viktor.kh.dev.shoplist.repository.db.data



import androidx.room.TypeConverters

import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter


@TypeConverters(ProductsConverter::class)
data class DataProduct(
   val name: String?,
   val date: String?,
   val ready : String?
)
