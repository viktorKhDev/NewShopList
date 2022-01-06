package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe

@Database(entities = [DataRecipe::class], version = 1)
@TypeConverters(ProductsConverter::class)
abstract class RecipesDB: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}