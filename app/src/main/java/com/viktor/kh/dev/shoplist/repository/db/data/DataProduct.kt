package com.viktor.kh.dev.shoplist.repository.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DataProduct(

        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "name") val name: String?,
        @ColumnInfo(name = "date")  val date: String?,
        @ColumnInfo(name = "ready") val ready : Boolean?
)
