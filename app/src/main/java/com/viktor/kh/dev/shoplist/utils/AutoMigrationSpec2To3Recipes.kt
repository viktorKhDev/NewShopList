package com.viktor.kh.dev.shoplist.utils

import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import org.json.JSONArray
import org.json.JSONTokener

class AutoMigrationSpec2To3Recipes :AutoMigrationSpec {



    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        super.onPostMigrate(db)
        db.query("SELECT * FROM dataRecipe ").use { cursor ->
            val idIndex = cursor.getColumnIndex("id")
            val productsIndex = cursor.getColumnIndex("products")

            while (cursor.moveToNext()){
                val id = cursor.getInt(idIndex)
                val products = convertProducts(cursor.getString(productsIndex))

                db.update(
                    "dataRecipe",
                    SQLiteDatabase.CONFLICT_NONE,
                    contentValuesOf(
                        "products" to products

                    ),
                    "id = ?",
                    arrayOf(id.toString())
                )
            }


        }
    }


    private fun convertProducts(productsJson :String):String{
        // update json with products, add color every product, default = null
        val jsonArray = JSONTokener(productsJson).nextValue() as JSONArray
        val productsList = mutableListOf<DataProduct>()
        for (i in 0 until jsonArray.length()){
            val product = DataProduct(
                name = jsonArray.getJSONObject(i).getString("name"),
                date = jsonArray.getJSONObject(i).getLong("date"),
                ready = jsonArray.getJSONObject(i).getBoolean("ready") ,
                amount = jsonArray.getJSONObject(i).getString("amount"),
                color = null
            )
            productsList.add(product)
        }

        return Gson().toJson(productsList)
    }

}