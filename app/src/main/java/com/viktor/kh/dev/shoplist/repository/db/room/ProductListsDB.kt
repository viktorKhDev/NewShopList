package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists


@Database(entities = [DataProductLists::class], version = 1)
abstract class ProductListsDB {
    abstract class ProductsDB: RoomDatabase() {
        abstract fun productsDao():ProductListsDao
    }
}