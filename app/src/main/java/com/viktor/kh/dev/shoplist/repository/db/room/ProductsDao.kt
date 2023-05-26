package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.*
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData


@Dao
interface ProductsDao {

    @Update
    fun update(dataProducts: ProductData)

    @Delete
    fun delete(dataProducts: ProductData)

    @Insert
    fun insert(dataProducts: ProductData)


    @Transaction
    fun addProducts(list: List<ProductData>){
        for (i in list){
            insert(i)
        }
    }

    @Query("DELETE FROM productData")
    fun clearData()


    @Query("DELETE FROM productData WHERE parentID = :listID")
    fun deleteProductsForList(listID: Int)


    @Transaction
    fun updateTableForList(list: List<ProductData>){
        val listID = list[0].parentID
        listID?.let { deleteProductsForList(it) }
        addProducts(list)
    }
}