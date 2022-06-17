package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList


@Dao
interface ProductListsDao {
    @Query("SELECT * FROM dataProductList")
    fun getAll(): List<DataProductList>

    @Query("SELECT * FROM dataProductList WHERE id = :listID")
    fun get(listID: Int):DataProductList

    @Update
    fun update(dataProducts: DataProductList)

    @Delete
    fun delete(dataProducts: DataProductList)

    @Insert
    fun insert(dataProducts: DataProductList)

}
