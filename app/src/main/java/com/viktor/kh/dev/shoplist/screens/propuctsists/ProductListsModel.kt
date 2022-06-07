package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.helpers.currentTimeToLong
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProductListsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var productListsDao: ProductListsDao

   val dataLists : MutableLiveData <List<DataProductList>> by lazy {
       MutableLiveData <List<DataProductList>>().also {
           getLists()
       }
   }


   fun getLists(): List<DataProductList> {
      return productListsDao.getAll()
    }

    fun updateList(){

    }

    fun deleteList(position : Int){
        var list = dataLists.value as MutableList<DataProductList>?
        if (list!=null){
            productListsDao.delete(list[position])
            list.removeAt(position)
            dataLists.value = list
        }
    }

    fun addList(name: String){
        var list = dataLists.value as MutableList<DataProductList>?
        if (list!=null){
           val listProduct :List<DataProduct> = emptyList()
            val productList = DataProductList(0,name, currentTimeToLong(),listProduct)
            list.add(productList)
            dataLists.value = list

        }
    }





}