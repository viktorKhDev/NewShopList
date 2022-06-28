package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.helpers.currentTimeToLong
import com.viktor.kh.dev.shoplist.helpers.listId
import com.viktor.kh.dev.shoplist.helpers.listName
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductListsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var productListsDao: ProductListsDao


   val dataLists : MutableLiveData <List<DataProductList>> by lazy {
       MutableLiveData <List<DataProductList>>().also {
           getLists()
       }
   }


    private fun getLists(){
        // get all list from DB
       CoroutineScope(Dispatchers.IO).launch {
     dataLists.postValue(productListsDao.getAll())
           Log.d("MyLog", dataLists.value?.size.toString())
           Log.d("MyLog", "getLists() in model")
       }

    }


    fun deleteList(position : Int){
        //delete list on position
        CoroutineScope(Dispatchers.IO).launch {
                productListsDao.delete(dataLists.value!![position])
            dataLists.postValue(productListsDao.getAll())
        }


    }

    fun addList(name: String){
        //add list with name
        val listProduct :List<DataProduct> = emptyList()
        val productList = DataProductList(0,name, currentTimeToLong(),listProduct)
        CoroutineScope(Dispatchers.IO).launch {
            productListsDao.insert(productList)
            dataLists.postValue(productListsDao.getAll())
        }

    }

    fun setList(position: Int,name: String){
        val list : DataProductList = dataLists.value!![position]
        CoroutineScope(Dispatchers.IO).launch {
         productListsDao.update(
             DataProductList(
             list.id, name, list.date,list.products
         )
         )
            dataLists.postValue(productListsDao.getAll())
      }
    }

    fun openList(controller: NavController, dataProductList: DataProductList){
        // open list on position
        var bundle = Bundle()
       bundle.putInt(listId,dataProductList.id)
        bundle.putString(listName,dataProductList.name)
        controller.navigate(R.id.action_productListsFragment_to_productsFragment,bundle)
    }

}

