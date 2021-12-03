package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.viktor.kh.dev.shoplist.repository.db.data.DataProducts

@Dao
interface ProductsDao {

    @Query("SELECT * FROM dataproducts")
    fun getAll(): List<DataProducts>

    @Delete
    fun delete(dataProducts: DataProducts)

    @Insert
    fun insert(dataProducts: DataProducts)

}