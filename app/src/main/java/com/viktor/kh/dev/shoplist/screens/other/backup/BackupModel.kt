package com.viktor.kh.dev.shoplist.screens.other.backup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.BackupData
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.synchronized
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BackupModel @Inject constructor(application: Application) : AndroidViewModel(application) {


    private val app = application
    @Inject
     lateinit var productListsDao: ProductListsDao
    @Inject
     lateinit var recipesDao: RecipesDao



    fun createFile(uri: Uri){
        //create backup file

        CoroutineScope(Dispatchers.IO).launch {
            runBlocking(Dispatchers.IO) {
                val lists = productListsDao.getAll()
                val recipes = recipesDao.getAll()
                val backupData = BackupData(lists,recipes)
                val backupString: String = Json.encodeToString(backupData)

                val pdf = app.contentResolver.openFileDescriptor(uri,"w")
                val objectOutputStream = ObjectOutputStream(FileOutputStream(pdf!!.fileDescriptor))
                try {
                    objectOutputStream.writeObject(backupString)
                    objectOutputStream.close()
                    pdf.close()

                    withContext(Dispatchers.Main){
                        Toast.makeText(app,app.getString(R.string.backup_file_created),Toast.LENGTH_SHORT)
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }finally {
                    objectOutputStream.close()
                    pdf.close()
                }
            }
            }

    }


   fun readFile(uri: Uri){
       //read backup file
       CoroutineScope(Dispatchers.IO).launch {
           runBlocking(Dispatchers.IO) {
               val objectInputStream = ObjectInputStream(app.contentResolver.openInputStream(uri))

               try {
                   val data: String = objectInputStream.readObject() as String
                   val backupData: BackupData = Json.decodeFromString(data)
                   objectInputStream.close()
                   if (data!=null){
                       productListsDao.clearAllTable()
                       productListsDao.updateTable(backupData.lists)
                       recipesDao.clearAllTable()
                       recipesDao.updateTable(backupData.recipes)
                   }
               }catch (e :Exception){
                   e.printStackTrace()
               }finally {
                   objectInputStream.close()
               }
           }

       }
   }
}