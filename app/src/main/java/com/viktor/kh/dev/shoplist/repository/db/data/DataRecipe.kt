package com.viktor.kh.dev.shoplist.repository.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DataRecipe (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "date")  val date: String?
)