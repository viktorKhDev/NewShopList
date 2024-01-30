package com.viktor.kh.dev.shoplist.repository.db.room.migrationUtils

import androidx.room.DeleteColumn

@DeleteColumn(tableName = "RecipesDB", columnName = "products")
class AutoMigrationRecipesFrom2To3 {
}