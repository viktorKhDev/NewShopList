package com.viktor.kh.dev.shoplist.repository.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter


@Entity

data class DataProductList(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val name: String?,
        val date:Long,
        @TypeConverters(ProductsConverter::class)
        val products: List<DataProduct>?
)
