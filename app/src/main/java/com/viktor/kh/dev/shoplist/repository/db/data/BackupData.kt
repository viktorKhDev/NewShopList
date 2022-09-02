package com.viktor.kh.dev.shoplist.repository.db.data

import kotlinx.serialization.Serializable


@Serializable
data class BackupData(
    val lists:List<DataProductList>,
    val recipes: List<DataRecipe>
)