package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.AutoMigration
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.utils.AutoMigrationSpec2To3Recipes

@Database(entities = [DataRecipe::class], version = 3,
        autoMigrations = [AutoMigration(from = 1, to = 2),
            AutoMigration(from = 2, to = 3, spec = AutoMigrationSpec2To3Recipes::class)])
@TypeConverters(ProductsConverter::class)
abstract class RecipesDB: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}