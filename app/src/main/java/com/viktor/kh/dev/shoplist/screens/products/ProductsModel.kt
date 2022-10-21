package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var productListsDao: ProductListsDao
  @Inject lateinit var recipeDao: RecipesDao
  private lateinit var listRecipes: List<DataRecipe>

  private var listId :Int? = null

    //variable for check start animation
    var initAnim = false
    var stateChange = UPDATE_DATA
    var animPosition = -1
    var forScrollToPosition = 0
    var productAdded: DataProduct? = null


    val productsList : MutableLiveData<List<DataProduct>> by lazy {
      MutableLiveData<List<DataProduct>>().also {
          getProducts()
      }
  }
    fun init(id: Int){
        stateChange = UPDATE_DATA
        animPosition = -1
        initAnim = true
        setRecipeList()
        if (listId!=id) {
            listId = id
        }
        getProducts()

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
              , currentProduct.ready?.let { changeReady(it) },currentProduct.amount)
          val list: DataProductList = productListsDao.get(listId!!)
          val products  = mutableListOf<DataProduct>()
          list.products?.let { products.addAll(it) }
          products.removeAt(position)
          products.add(newProduct)
          productListsDao.update(DataProductList(list.id,list.name,list.date,products))
          stateChange = CHANGE_READY
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
            stateChange = UPDATE_DATA
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
            stateChange = DELETE_PRODUCT
            animPosition = position
         getProducts()
     }
    }

    fun addProduct(productName: String){
      initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val list: DataProductList = productListsDao.get(listId!!)
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong(), false,"")
            productAdded = dataProduct
            val products  = mutableListOf<DataProduct>()
            list.products?.let { products.addAll(it) }
            products.add(dataProduct)
            productListsDao.update(DataProductList(list.id,list.name,list.date,products))
            stateChange = ADD_PRODUCT
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
           stateChange = UPDATE_DATA
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
                 stateChange =  UPDATE_DATA
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
         }else{
             showToast(context.getString(R.string.list_is_empty),context)
         }

    }
    fun addListFromRecipe(listClicked:List<Boolean>,portions: Int){
        initAnim = false

        CoroutineScope(Dispatchers.IO).launch {
            var readyList = mutableListOf<DataProduct>()
            for (i in listClicked.indices){
                if (listClicked[i]){

                    if (listRecipes[i].products!=null){

                        for (e in listRecipes[i].products!!){
                            val product = DataProduct(
                                "${e.name} (${listRecipes[i].name})"
                                ,e.date
                                ,false
                                , e.amount?.let { countPortions(it,portions) }
                            )
                            readyList.add(product)
                        }

                    }
                }
            }
            val list: DataProductList = productListsDao.get(listId!!)
            list.products?.let { readyList.addAll(it) }
            productListsDao.update(DataProductList(list.id,list.name,list.date,readyList))
            stateChange = UPDATE_DATA
            getProducts()

        }
    }


    private fun countPortions(text:String,value:Int):String{
        //logic for count portions in amount

        val text1 = text.filterNot { it.isDigit() }.replace(".","",false)

        val patternNumber = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+")

        val matcherNumber = patternNumber.matcher(text)

        val sb = StringBuilder()
        while (matcherNumber.find()) {
               sb.append((matcherNumber.group().toDouble()*value).toString())
               sb.append(" ")
        }

        return "$sb $text1".replace("  "," ")
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
            if (sortByDate) {
                sortedList = products.sortedWith(compareBy({it.ready}, { it.date }))

            }else{
                sortedList = products.sortedWith(compareBy({ it.ready }, { it.name }))
            }
            if (stateChange == ADD_PRODUCT&&productAdded!=null){
                for (i in products.indices){
                    if (compareProduct(productAdded!!,sortedList[i])){
                        forScrollToPosition = i
                        animPosition = i
                    }
                }
            }

            return sortedList
        }else{
            return products
        }



    }

    private fun compareProduct(product1:DataProduct,product2: DataProduct):Boolean{
        return (product1.name==product2.name
                &&product1.date == product2.date
                )

    }



}