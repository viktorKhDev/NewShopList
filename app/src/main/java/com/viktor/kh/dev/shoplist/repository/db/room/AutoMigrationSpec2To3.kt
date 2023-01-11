package com.viktor.kh.dev.shoplist.repository.db.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase

class AutoMigrationSpec2To3 :AutoMigrationSpec {

    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        super.onPostMigrate(db)
       db.query("SELECT * FROM dataProductList ").use { cursor ->
           val idIndex = cursor.getColumnIndex("id")
           val productsIndex = cursor.getColumnIndex("products")

           while (cursor.moveToNext()){
              val id = cursor.getInt(idIndex)
               val products = convertProducts(cursor.getString(productsIndex))

               db.update(
                   "dataProductList",
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


    private fun convertProducts(prodJson :String):String{
        // todo update json with products, add color every product, default = 0
     return ""
    }

}