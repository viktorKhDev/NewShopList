package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProductListsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var productListsDao: ProductListsDao

   val dataLists : MutableLiveData <List<DataProductLists>> by lazy {
       MutableLiveData <List<DataProductLists>>().also {
           getLists()
       }
   }


   fun getLists(): List<DataProductLists> {
      return productListsDao.getAll()
    }

    fun updateLists(){

    }



}