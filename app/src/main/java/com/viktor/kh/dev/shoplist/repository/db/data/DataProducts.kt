package com.viktor.kh.dev.shoplist.repository.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class DataProducts(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "products") val products: List<DataProduct>?,
    @ColumnInfo(name = "date")  val date: Date?

    )
