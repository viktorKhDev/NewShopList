package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData


@Dao
interface ProductsDao {

    @Update
    fun update(dataProducts: ProductData)

    @Delete
    fun delete(dataProducts: ProductData)

    @Insert
    fun insert(dataProducts: ProductData)
}