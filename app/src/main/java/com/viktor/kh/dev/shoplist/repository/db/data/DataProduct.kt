package com.viktor.kh.dev.shoplist.repository.db.data



import com.google.gson.annotations.SerializedName



data class DataProduct(
        @SerializedName("name") val name: String?,
        @SerializedName("date") val date: Long?,
        @SerializedName("ready") val ready : Boolean?
)
