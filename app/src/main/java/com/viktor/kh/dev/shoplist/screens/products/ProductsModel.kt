package com.viktor.kh.dev.shoplist.screens.products



import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.repository.db.room.ProductsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class ProductsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

  //@Inject lateinit var productListsDao: ProductListsDao
  @Inject lateinit var recipeDao: RecipesDao
  @Inject lateinit var productsDao: ProductsDao
  private lateinit var listRecipes: List<DataRecipe>

  private var listId :Int? = null

    //variable for check start animation
    var initAnim = false
    var stateChange = UPDATE_DATA
    var animPosition = -1
    var forScrollToPosition = 0
    var productAdded: ProductData? = null
    var currentColor: Int? = null
    private val app = application


    val productsList : MutableLiveData<List<ProductData>> by lazy {
      MutableLiveData<List<ProductData>>().also {
          getProducts()
      }
  }


    val dataColors :MutableLiveData<ColorClickedList> by lazy {
        MutableLiveData<ColorClickedList>()
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
        dataColors.value = ColorClickedList()
    }



   fun clickColor(position: Int){
        val data = dataColors.value
        data!!.clickColor(position)
        dataColors.value = data
        dataColors.hasActiveObservers()
        currentColor = dataColors.value!!.getCurrentColor()
    }


    fun clickColorWithColor(color: Int?){
        if (color!=null){
            val data = dataColors.value
            data!!.clickColorWithColor(color)
            dataColors.value = data
            dataColors.hasActiveObservers()
            currentColor = dataColors.value!!.getCurrentColor()
        }else{
            dataColors.value!!.clearClick()
            dataColors.hasActiveObservers()
            currentColor = dataColors.value!!.getCurrentColor()
        }
    }


    fun clearClick(){
        dataColors.value!!.clearClick()
        dataColors.hasActiveObservers()
    }


    private fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            val data : List<ProductData> = productsDao.getProductsForList(listId!!,false)
            val sortData = sortProducts(data)
            withContext(Dispatchers.Main){
                productsList.value = sortData
            }
        }
    }

    fun changeReady(position :Int){
       initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
          val currentProduct = productsList.value!![position]
            val newProduct = ProductData(
               currentProduct.id,
                currentProduct.name,
                currentProduct.date,
                changeReady(currentProduct.ready!!),
                currentProduct.amount,
                currentProduct.productColor,
                currentProduct.parentID,
                currentProduct.isRecipe
            )

            productsDao.update(newProduct)


          stateChange = CHANGE_READY
          animPosition = position
          getProducts()
      }
    }

    fun renameProduct(position: Int,name: String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {

            var currentProduct = productsList.value!![position]
            val newProduct = ProductData(
                currentProduct.id,
                name,
                currentProduct.date,
                currentProduct.ready,
                currentProduct.amount,
                currentColor,
                currentProduct.parentID,
                currentProduct.isRecipe
            )

            productsDao.update(newProduct)
            stateChange = UPDATE_DATA
            getProducts()
        }
    }

    fun deleteProduct(position: Int){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {

            productsDao.delete(productsList.value!![position])
            stateChange = DELETE_PRODUCT
            animPosition = position
         getProducts()
     }
    }



    fun addProduct(productName: String){
      initAnim = false

        CoroutineScope(Dispatchers.IO).launch {
            val product = ProductData(0,productName.trim(), currentTimeToLong(),
                false,"",currentColor,listId,false)

            productAdded = product
            productsDao.insert(product)
            stateChange = ADD_PRODUCT
            productsList.value?.let { animPosition = it.size }
            getProducts()

        }
    }



    fun cleanList(){
        CoroutineScope(Dispatchers.IO).launch {
            productsDao.deleteProductsForList(listId!!,false)
            stateChange = UPDATE_DATA
           getProducts()


       }
    }

     fun pasteList(){
         initAnim = false
         CoroutineScope(Dispatchers.IO).launch {
            // val products = mutableListOf<DataProduct>()
             val text: String = getClipboard(getApplication())
             if (text.isNotEmpty()){
                 val strings = text.split("\n").toTypedArray()
                 //val list: DataProductList = productListsDao.get(listId!!)
                 val copyList  = mutableListOf<ProductData>()
                // products.addAll(list.products!!)
                 for (name in strings) {
                     if (name.isNotEmpty()){
                         val product = ProductData(0,
                             name.trim(),
                             currentTimeToLong(),
                             false,
                             "",
                             null,
                             listId,
                             false)
                         copyList.add(product)
                     }
                 }


                 productsDao.addProducts(copyList)
                 //
                 stateChange =  UPDATE_DATA
                 getProducts()

                 productsList.value?.let { animPosition = it.size }

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
            var readyList = mutableListOf<ProductData>()
            for (i in listClicked.indices){

                if (listClicked[i]){

                    val productsFromRecipe = productsDao.getProductsForList(listRecipes[i].id,true)
                    if (productsFromRecipe.isNotEmpty()){
                        for (i in productsFromRecipe){
                            val product = ProductData(0,
                                i.name,
                                currentTimeToLong(),
                                false,
                                i.amount?.let { countPortions(it,portions) },
                                null,
                                listId,
                                false
                                )

                            readyList.add(product)
                        }
                    }
                }
            }
            productsDao.addProducts(readyList)
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
               sb.append("")
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

    private  fun sortProducts(products: List<ProductData>):List<ProductData>{
        /*    if (products.isNotEmpty()&&products.size!=1){
              val sortedList: List<DataProduct>

            when(sortItems){
                  app.getString(R.string.time) ->{
                      sortedList = products.sortedWith(compareBy({it.ready}, { it.date }))
                  }
                  app.getString(R.string.title) -> {
                      sortedList = products.sortedWith(compareBy({ it.ready }, { it.name }))
                  }
                  app.getString(R.string.color_time_pref) ->{
                      sortedList = products.sortedWith(compareBy({ it.ready }, { it.color },{it.date}))
                  }
                  app.getString(R.string.color_title_pref) -> {
                      sortedList = products.sortedWith(compareBy({ it.ready }, { it.color },{it.name}))
                  }
                  else -> {
                      sortedList = products.sortedWith(compareBy({ it.ready }, { it.date }))
                  }
              }
              if (stateChange == ADD_PRODUCT&&productAdded!=null){
                  for (i in products.indices){
                      if (compareProduct(productAdded!!,sortedList[i])){
                          forScrollToPosition = i
                          animPosition = i
                      }
                  }
              }*/


        if (products.isNotEmpty()&&products.size!=1){
            val sortedList: List<ProductData>
            if (sortByDate) {
                sortedList = products.sortedWith(compareBy({it.ready}, { it.date }))

            }else{
                sortedList = products.sortedWith(compareBy({ it.ready }, { it.name!!.lowercase() }))
            }
            if (stateChange == ADD_PRODUCT &&productAdded!=null){
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

    private fun compareProduct(product1:ProductData,product2: ProductData):Boolean{
        return (product1.name==product2.name
                &&product1.date == product2.date
                )

    }



}