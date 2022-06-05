package com.viktor.kh.dev.shoplist.repository.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import org.jetbrains.annotations.Nullable


@Entity
data class DataProductList(
        @PrimaryKey val id: Int,
        val name: String?,
        val date: Long?,
        @TypeConverters(ProductsConverter::class)
        val products: List<DataProduct>?
)
