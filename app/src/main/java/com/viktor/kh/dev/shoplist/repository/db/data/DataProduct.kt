package com.viktor.kh.dev.shoplist.repository.db.data



import androidx.room.ColumnInfo
import androidx.room.TypeConverters

import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter


@TypeConverters(ProductsConverter::class)
data class DataProduct(
   @ColumnInfo(name = "name" ) val name: String?,
   @ColumnInfo(name = "date" ) val date: Long?,
   @ColumnInfo(name = "ready" ) val ready: Boolean?,
   @ColumnInfo(name = "amount" ) val amount: String?
   )
