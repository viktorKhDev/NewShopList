package com.viktor.kh.dev.shoplist.repository.db.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class DataProductList(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id" )val id: Int = 0,
        @ColumnInfo(name = "name" ) val name: String?,
        @ColumnInfo(name = "date" ) val date: Long?,
        @ColumnInfo(name = "color") val color: Int?,
        )
