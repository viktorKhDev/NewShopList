package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists


@Dao
interface ProductListsDao {
    @Query("SELECT * FROM dataProductLists")
    fun getAll(): List<DataProductLists>

    @Delete
    fun delete(dataProducts: DataProductLists)

    @Insert
    fun insert(dataProducts: DataProductLists)
}
