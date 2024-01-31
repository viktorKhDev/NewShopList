package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
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

    @Query("SELECT * FROM productData")
    fun getAll(): List<ProductData>


    @Query("DELETE FROM productData")
    fun clearData()

    @Query("SELECT * FROM productData WHERE parentID = :listID AND isRecipe = :isRecipe")
    fun getProductsForList(listID: Int, isRecipe: Boolean): List<ProductData>


    @Query("DELETE FROM productData WHERE parentID = :listID AND isRecipe = :isRecipe")
    fun deleteProductsForList(listID: Int, isRecipe: Boolean)


    @Transaction
    fun updateTableForList(list: List<ProductData>,isRecipe: Boolean){
        val listID = list[0].parentID
        listID?.let { deleteProductsForList(it,isRecipe) }
        addProducts(list)
    }
}