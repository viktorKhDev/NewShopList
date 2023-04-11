package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData

@Database(entities = [ProductData::class], version = 1)
abstract class ProductsDB : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}