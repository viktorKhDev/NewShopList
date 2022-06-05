package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList


@Dao
interface ProductListsDao {
    @Query("SELECT * FROM dataProductList")
    fun getAll(): List<DataProductList>

    @Delete
    fun delete(dataProducts: DataProductList)

    @Insert
    fun insert(dataProducts: DataProductList)
}
