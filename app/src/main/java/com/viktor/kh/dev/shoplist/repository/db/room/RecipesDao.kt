package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
@Dao
interface RecipesDao {

    @Query("SELECT * FROM dataRecipe")
    fun getAll(): List<DataRecipe>

    @Delete
    fun delete(dataRecipe: DataRecipe)

    @Insert
    fun insert(dataRecipe: DataRecipe)

    @Update
    fun update(dataRecipe: DataRecipe)

    @Query("SELECT * FROM dataRecipe WHERE id = :listID")
    fun get(listID: Int):DataRecipe

}