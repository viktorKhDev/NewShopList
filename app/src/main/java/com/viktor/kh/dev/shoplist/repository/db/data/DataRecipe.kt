package com.viktor.kh.dev.shoplist.repository.db.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class DataRecipe (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id" ) val id: Int = 0,
    @ColumnInfo(name = "name" ) val name: String?,
    @ColumnInfo(name = "text" ) val text: String?,
    @ColumnInfo(name = "date" ) val date: Long?,
    @TypeConverters(ProductsConverter::class)
    @ColumnInfo(name = "products" ) val products: List<DataProduct>?
)