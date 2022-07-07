package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.helpers.*
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

    //variable for check start animation
    var initAnim = false
    var stateChange = updateData
    var animPosition = -1
    //need get from settings
   private  var typeSortProduct = sortByName


  val productsList : MutableLiveData<List<DataProduct>> by lazy {
      MutableLiveData<List<DataProduct>>().also {
          getProducts()
      }
  }
    fun init(id: Int){
        stateChange = updateData
        Log.d("fixLog", "state in model = $stateChange  ")
        animPosition = -1
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
          stateChange = changeReady
          animPosition = position
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
            stateChange = updateData
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
            stateChange = deleteProduct
            animPosition = position
         getProducts()
     }
    }

    fun addProduct(productName: String){
      initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val list: DataProductList = productListsDao.get(listId!!)
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong().toString(), falce0)
            val products  = mutableListOf<DataProduct>()
            list.products?.let { products.addAll(it) }
            products.add(dataProduct)
            productListsDao.update(DataProductList(list.id,list.name,list.date,products))
            stateChange = addProduct
            animPosition = products.size-1
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
           stateChange = updateData
           getProducts()

       }
    }

     fun pasteList(){
         initAnim = false
         CoroutineScope(Dispatchers.IO).launch {
             val products = mutableListOf<DataProduct>()
             val text: String = getClipboard(getApplication())
             if (text.isNotEmpty()){
                 val strings = text.split("\n").toTypedArray()
                 val list: DataProductList = productListsDao.get(listId!!)
                 products.addAll(list.products!!)
                 for (name in strings) {
                     val product = DataProduct(name.trim(), currentTimeToLong().toString(), falce0)
                     products.add(product)
                     animPosition = products.size
                 }
                 productListsDao.update(DataProductList(list.id,list.name,list.date,products))
                 stateChange =  updateData
                 getProducts()
             }

         }

     }

     fun shareList(context: Context){
         val list = productsList.value
         val stringBuilder = StringBuilder()
         if (list!!.isNotEmpty()){
             for (product in list) {
                 stringBuilder.append(product.name)
                 stringBuilder.append("\n")
             }
             shareText(stringBuilder.toString(),context)
         }

    }
    fun addListFromRecipe(){

    }

    private fun changeReady(state :String):String{
        return if (state==true1) falce0 else true1
    }

    private  fun sortProducts(products: List<DataProduct>):List<DataProduct>{
        if (products.isNotEmpty()&&products.size!=1){
            val sortedList: List<DataProduct>
            val product: DataProduct
            if (typeSortProduct == sortByName) {
                sortedList = products.sortedWith(compareBy({ it.ready }, { it.name }))
            }else{
                sortedList = products.sortedBy { it.ready }
            }

            return sortedList
        }else{
            return products
        }



    }




   /* private fun checkIdenticalName(s: String, productList :DataProductList): String{
        // logic for the name if the list of names contains such a name
        var newName: String

        val products  = mutableListOf<DataProduct>()
        productList.products?.let { products.addAll(it) }
         val list  = mutableListOf<String>()
        for (i in products){
               list.add(i.name.toString())
           }
        val r = Regex("\\(\\d\\)")
        var sameNumber = 2
        var countDownsize = 2
        if (list.contains(s)){
            if (s.equals(r)){
                val symbols = s.toCharArray()
                val numbers = mutableListOf<Char>()
                for (i in symbols.size-2 downTo 1){
                    countDownsize++
                    if (symbols[i]=='(') break
                    numbers.add(symbols[i])
                }
                numbers.reverse()
                var str =  StringBuilder()
                for(i in numbers) {
                    str.append(i)
                }
                sameNumber = str.toString().toInt()
                newName = "${s.substring(0,s.length - countDownsize)}($sameNumber)"

            }else{
                newName = "$s ($sameNumber)"
            }

        }else{
            newName = s
        }

        return newName
    }*/


}