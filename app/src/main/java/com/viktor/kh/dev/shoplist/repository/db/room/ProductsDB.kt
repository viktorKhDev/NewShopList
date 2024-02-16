package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.repository.db.room.migrationUtils.AutoMigrationProductsFrom1to2

@Database(entities = [ProductData::class], version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AutoMigrationProductsFrom1to2::class)])
abstract class ProductsDB : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}