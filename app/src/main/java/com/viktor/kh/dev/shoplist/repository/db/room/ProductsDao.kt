package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe


@Dao
interface ProductsDao {
    @Query("SELECT * FROM dataproduct")
    fun getAll(): List<DataProduct>

    @Delete
    fun delete(dataProducts: DataProduct)

    @Insert
    fun insert(dataProducts: DataProduct)
}