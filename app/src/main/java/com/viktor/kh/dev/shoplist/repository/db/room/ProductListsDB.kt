package com.viktor.kh.dev.shoplist.repository.db.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.viktor.kh.dev.shoplist.repository.db.converters.ProductsConverter
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.utils.AutoMigrationSpec2To3

@Database(entities = [DataProductList::class], version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3, spec = AutoMigrationSpec2To3::class)]
)
@TypeConverters(ProductsConverter::class)
abstract class ProductListsDB: RoomDatabase() {
    abstract fun productListsDao ():ProductListsDao
}