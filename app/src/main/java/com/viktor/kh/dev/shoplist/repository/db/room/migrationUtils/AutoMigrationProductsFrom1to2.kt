package com.viktor.kh.dev.shoplist.repository.db.room.migrationUtils

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec


@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "ProductData",
        columnName = "color"
    )
)
class AutoMigrationProductsFrom1to2:AutoMigrationSpec {
}