package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun updateLists(){

    }

    fun deleteList(position : Int){

    }

    fun addList(){

    }





}