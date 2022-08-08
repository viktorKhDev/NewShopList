package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.utils.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao
  @Inject lateinit var recipeDao: RecipesDao
  private lateinit var listRecipes: List<DataRecipe>

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
        animPosition = -1
        initAnim = true
        if (listId!=id) {
            listId = id
            getProducts()
           setRecipeList()
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
              , currentProduct.ready?.let { changeReady(it) },"")
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
            val newProduct = DataProduct(name,currentProduct.date,currentProduct.ready,"")
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
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong(), false,"")
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
                     val product = DataProduct(name.trim(), currentTimeToLong(), false,"")
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
    fun addListFromRecipe(position: Int){
        initAnim = false




        stateChange = updateData
    }

   private fun setRecipeList(){
       CoroutineScope(Dispatchers.IO).launch {
          listRecipes = recipeDao.getAll()
        }
    }


    fun getRecipesList():List<DataRecipe>{
      return listRecipes
    }

    private fun changeReady(state :Boolean):Boolean{
        return if (state==true) false else true
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





}