package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsModel @Inject constructor(application: Application,val listId : Int) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao

  val productsList : MutableLiveData<List<DataProduct>> by lazy {
      MutableLiveData<List<DataProduct>>().also {
          getProducts()
      }
  }




    fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            productsList.postValue(productListsDao.openList(listId))
        }
    }
}