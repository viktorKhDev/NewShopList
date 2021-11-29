package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe

@Database(entities = [DataRecipe::class], version = 1)
abstract class RecipesDB: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}