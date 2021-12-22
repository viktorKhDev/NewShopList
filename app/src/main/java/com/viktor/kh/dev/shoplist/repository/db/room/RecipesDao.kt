package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
@Dao
interface RecipesDao {

    @Query("SELECT * FROM datarecipe")
    fun getAll(): List<DataRecipe>

    @Delete
    fun delete(dataProducts: DataRecipe)

    @Insert
    fun insert(dataProducts: DataRecipe)
}