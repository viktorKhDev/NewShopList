package com.viktor.kh.dev.shoplist.screens.other.backup

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.BackupData
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import com.viktor.kh.dev.shoplist.utils.showToast
import com.viktor.kh.dev.shoplist.utils.writeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
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
if (uri!="".toUri()){
    CoroutineScope(Dispatchers.IO).launch {
        val lists = productListsDao.getAll()
        val recipes = recipesDao.getAll()
        val backupData = BackupData(lists,recipes)
        val backupString: String = Json.encodeToString(backupData)

        try {
            val pdf = app.contentResolver.openFileDescriptor(uri,"w")
            val objectOutputStream = ObjectOutputStream(FileOutputStream(pdf!!.fileDescriptor))
            objectOutputStream.writeObject(backupString)
            objectOutputStream.close()
            pdf.close()
            writeLog("write backup file to ${uri.toString()}",app,false)
            withContext(Dispatchers.Main){
                    showToast(app.getString(R.string.backup_file_created),app)

            }

        }catch (e : Exception){
            writeLog("write backup file to ${uri.toString()} error = ${e.stackTrace.toString()}",app,true)
            withContext(Dispatchers.Main){
                showToast(app.getString(R.string.error),app)
            }

        }

    }
}


    }


   fun readFile(uri: Uri){
       //read backup file
       if(uri!="".toUri()){
           CoroutineScope(Dispatchers.IO).launch {

               try {
                   val objectInputStream = ObjectInputStream(app.contentResolver.openInputStream(uri))
                       val data: String = objectInputStream.readObject() as String
                       val backupData: BackupData = Json.decodeFromString(data)
                       objectInputStream.close()
                       if (data!=null){
                           productListsDao.clearAllTable()
                           productListsDao.updateTable(backupData.lists)
                           recipesDao.clearAllTable()
                           recipesDao.updateTable(backupData.recipes)
                           writeLog("read backup file from ${uri.toString()}",app,false)
                           withContext(Dispatchers.Main){
                               showToast(app.getString(R.string.backup_read),app)

                           }

                       }else{
                           withContext(Dispatchers.Main){
                               writeLog("read backup file from, data = null ${uri.toString()}",app,false)
                               showToast(app.getString(R.string.error),app)

                           }
                       }
                   }catch (e :Exception){
                   writeLog("read backup file from ${uri.toString()} error = ${e.stackTrace.toString()}",app,true)
                   withContext(Dispatchers.Main){
                       showToast(app.getString(R.string.error),app)
                   }
                   }


           }
       }

   }
}