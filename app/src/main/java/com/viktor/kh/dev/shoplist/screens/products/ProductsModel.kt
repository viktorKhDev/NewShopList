package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.util.Log
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.helpers.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao

  private var listId :Int? = null

    //variable for check animation
    var initAnim = false

    //need get from settings
   private  var typeSortProduct = sortByName


  val productsList : MutableLiveData<List<DataProduct>> by lazy {
      MutableLiveData<List<DataProduct>>().also {
          getProducts()
      }
  }
    fun init(id: Int){
        initAnim = true
        if (listId!=id) {
            listId = id
            getProducts()

        }

        Log.d("MyLog", "productsModel init with id ${id.toString()}")
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
       initAnim = false
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
        initAnim = false
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
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
         val list: DataProductList = productListsDao.get(listId!!)
         val products  = mutableListOf<DataProduct>()
         list.products?.let { products.addAll(it) }
         products.removeAt(position)
         productListsDao.update(DataProductList(list.id,list.name,list.date,products))
         getProducts()
     }
    }

    fun addProduct(productName: String){
      initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong().toString(), falce0)
            val list: DataProductList = productListsDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            list.products?.let { products.addAll(it) }
            products.add(dataProduct)
           productListsDao.update(DataProductList(list.id,list.name,list.date,products))
            getProducts()
        }
    }






    fun cleanList(){
       CoroutineScope(Dispatchers.IO).launch {
           val list: DataProductList = productListsDao.get(listId!!)
           val products  = mutableListOf<DataProduct>()
           list.products?.let { products.addAll(it) }
           products.clear()
           productListsDao.update(DataProductList(list.id,list.name,list.date,products))
           getProducts()
       }
    }

     fun pasteList(){
         val currentData = mutableListOf<DataProduct>()

         val text: String = getClipboard(getApplication())
         val strings = text.split("\n").toTypedArray()

             for (name in strings) {
                 val product = DataProduct(name, currentTimeToLong().toString(), falce0)
                 currentData.add(product)
             }



    }

     fun shareList(){

    }
    fun addListFromRecipe(){

    }

    private fun changeReady(state :String):String{
        return if (state==true1) falce0 else true1
    }

    private  fun sortProducts(products: List<DataProduct>):List<DataProduct>{
        val sortedList: List<DataProduct>
        if (typeSortProduct == sortByName) {
            sortedList = products.sortedWith(compareBy({ it.ready }, { it.name }))
        }else{
            sortedList = products.sortedBy { it.ready }
        }
        return sortedList
    }

    private fun checkIdenticalName( productName: String): String{
        // logic for the name if the list of names contains such a name
        var newName: String = ""
         val list  = mutableListOf<String>()
        for (i in productsList.value!!){
               list.add(i.name.toString())
           }

        newName = changeIdenticalName(productName,list)


         return newName
    }

    private fun changeIdenticalName(s : String, list : List<String>):String{
        val r = Regex("\\(\\d\\)")

        var sameNumber = 2
        var newName = ""
        if (list.contains(s)){
            if (s.equals(r)){
                val symbols = s.toCharArray()
                val numbers = mutableListOf<Char>()
                for (i in symbols.size-2 downTo 1){
                    if (symbols[i]=='(') break
                   numbers.add(symbols[i])
                }
                numbers.reverse()
                var str =  StringBuilder()
                for(i in numbers) {
                    str.append(i)
                }
                sameNumber = str.toString().toInt()


            }else{
                newName = "$s ($sameNumber)"
            }

        }
        return newName
    }

}