package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao

  private var listId :Int? = null
    private val init = false

    fun init(id: Int){
        if (!init||listId!=id){
            listId = id
            getProducts()
        }

        Log.d("MyLog", "productsModel init with id ${id.toString()}")
    }

  val productsList : MutableLiveData<List<DataProduct>> by lazy {
      MutableLiveData<List<DataProduct>>().also {
          getProducts()
      }
  }



   private fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            val data : DataProductList = productListsDao.get(listId!!)
            productsList.postValue(data.products)
            Log.d("MyLog", "productsModel get list")
        }
    }


    fun changeReady(position :Int){

    }

    fun renameProduct(position: Int,name: String){

    }

    fun deleteProduct(position: Int){
     CoroutineScope(Dispatchers.IO).launch {
         val list: DataProductList = productListsDao.get(listId!!)
         val products  = mutableListOf<DataProduct>()
         list.products?.let { products.addAll(it) }
         products.removeAt(position)
         productListsDao.update(DataProductList(list.id,list.name,list.date,products))
         getProducts()
     }
    }

    fun addProduct(product: DataProduct){

        CoroutineScope(Dispatchers.IO).launch {
            val list: DataProductList = productListsDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            list.products?.let { products.addAll(it) }
            products.add(product)
           productListsDao.update(DataProductList(list.id,list.name,list.date,products))
            getProducts()
        }
    }


}