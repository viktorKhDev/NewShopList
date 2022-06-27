package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.helpers.falce0
import com.viktor.kh.dev.shoplist.helpers.sortByName
import com.viktor.kh.dev.shoplist.helpers.true1
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao

  private var listId :Int? = null
    private val init = false

    //need get from settings
   private  var typeSortProduct = sortByName

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
            val newData  = DataProductList(data.id,data.name,data.date,
                data.products?.let { sortProducts(it) })
            productListsDao.update(DataProductList(newData.id,newData.name,newData.date,newData.products))
            withContext(Dispatchers.Main){
                productsList.value = newData.products
            }

            Log.d("MyLog", "productsModel get list")
        }
    }


    fun changeReady(position :Int){
      CoroutineScope(Dispatchers.IO).launch {
          val currentProduct = productsList.value!![position]
          val newProduct = DataProduct(currentProduct.name,currentProduct.date
              , currentProduct.ready?.let { changeReady(it) })
          val list: DataProductList = productListsDao.get(listId!!)
          val products  = mutableListOf<DataProduct>()
          list.products?.let { products.addAll(it) }
          products.removeAt(position)
          products.add(newProduct)
          productListsDao.update(DataProductList(list.id,list.name,list.date,products))
          getProducts()
      }
    }

    fun renameProduct(position: Int,name: String){
        CoroutineScope(Dispatchers.IO).launch {
            val currentProduct = productsList.value!![position]
            val newProduct = DataProduct(name,currentProduct.date,currentProduct.ready)
            val list: DataProductList = productListsDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            list.products?.let { products.addAll(it) }
            products.removeAt(position)
            products.add(newProduct)
            productListsDao.update(DataProductList(list.id,list.name,list.date,products))
            getProducts()
        }
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


    private fun changeReady(state :String):String{
        return if (state==true1) falce0 else true1
    }

    private  fun sortProducts(products: List<DataProduct>):List<DataProduct>{
        var sortedList = emptyList<DataProduct>()
        if (typeSortProduct == sortByName) {
            sortedList = products.sortedWith(compareBy({ it.ready }, { it.name }))
        }else{
            sortedList = products.sortedBy { it.ready }
        }
      return sortedList
    }




}