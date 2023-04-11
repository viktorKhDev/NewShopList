package com.viktor.kh.dev.shoplist.repository.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity
data class ProductData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id" ) val id: Long = 0,
    @ColumnInfo(name = "name" ) val name: String?,
    @ColumnInfo(name = "date" ) val date: Long?,
    @ColumnInfo(name = "ready" ) val ready: Boolean?,
    @ColumnInfo(name = "amount" ) val amount: String?,
    @ColumnInfo(name = "color" ) val color: String,
    @ColumnInfo(name = "parentID" ) val parentID: Int?,
    @ColumnInfo(name = "isRecipe" ) val isRecipe: Boolean
)