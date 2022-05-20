package com.viktor.kh.dev.shoplist.repository




import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import dagger.hilt.EntryPoint
import javax.inject.Inject


@EntryPoint
class Repository @Inject constructor() {


  @Inject lateinit var productListsDao: ProductListsDao
  @Inject lateinit var recipesDao: RecipesDao


  lateinit var lists: MutableLiveData<List<DataProductLists>>
 init {
   getLists()
 }



    fun getLists(){
        lists.value = productListsDao.getAll()
    }


}