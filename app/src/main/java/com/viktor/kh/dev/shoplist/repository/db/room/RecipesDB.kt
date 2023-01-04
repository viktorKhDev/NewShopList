package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe

@Database(entities = [DataRecipe::class], version = 2,
        autoMigrations = [androidx.room.AutoMigration(from = 1, to = 2)])
@TypeConverters(ProductsConverter::class)
abstract class RecipesDB: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}