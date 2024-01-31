package com.viktor.kh.dev.shoplist.repository.db.room.migrationUtils

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "DataProductList",
        columnName = "products"
    )
)
class AutoMigrationListsFrom2To3: AutoMigrationSpec