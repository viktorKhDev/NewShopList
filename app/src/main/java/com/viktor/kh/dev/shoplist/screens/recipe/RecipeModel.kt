package com.viktor.kh.dev.shoplist.screens.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipeModel @Inject constructor(application: Application): AndroidViewModel(application) {

    @Inject lateinit var recipesDao: RecipesDao

    private var listId :Int? = null

    private val app = application
    //variable for check start animation
    var initAnim = false
    var stateChange = UPDATE_DATA
    var animPosition = -1
    var currentColor: Int? = null

    //need get from settings



    val productsList : MutableLiveData<List<DataProduct>> by lazy {
        MutableLiveData<List<DataProduct>>().also {
            getProducts()
        }
    }

    val recipeText : MutableLiveData<String>  by lazy {
        MutableLiveData<String>().also {
            getText()
        }
    }


    fun init(id: Int){
        stateChange = UPDATE_DATA
        if (listId!=id) {
            listId = id
        }
        getText()
    }



    private fun getText(){
        CoroutineScope(Dispatchers.IO).launch {
            val data : DataRecipe = recipesDao.get(listId!!)
            withContext(Dispatchers.Main){
                recipeText.value = data.text.toString()
            }

        }

    }




    fun saveText(text: String){
        CoroutineScope(Dispatchers.IO).launch {
            val data : DataRecipe = recipesDao.get(listId!!)
            val newData  = DataRecipe(data.id,data.name,text,data.date, data.color,
                data.products?.let { sortProducts(it) })
            recipesDao.update(newData)
            withContext(Dispatchers.Main){
              recipeText.value = newData.text
            }
        }
    }


    fun getProducts(){

        CoroutineScope(Dispatchers.IO).launch {
            val data : DataRecipe = recipesDao.get(listId!!)
            val newData  = DataRecipe(data.id,data.name,data.text,data.date,data.color,
                data.products?.let { sortProducts(it) })
            recipesDao.update(newData)
            withContext(Dispatchers.Main){
                productsList.value = newData.products
            }
        }
    }

    fun renameProduct(position: Int,name: String,amount: String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val currentProduct = productsList.value!![position]
            val newProduct = DataProduct(name,currentProduct.date, false,amount,currentColor)
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.removeAt(position)
            products.add(newProduct)
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,recipe.color,products))
            stateChange = UPDATE_DATA
            getProducts()
        }
    }

    fun deleteProduct(position: Int){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.removeAt(position)
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,recipe.color,products))
            stateChange = DELETE_PRODUCT
            animPosition = position
            getProducts()
        }
    }

    fun addProduct(productName: String,amount :String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong(), false,amount,currentColor)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.add(dataProduct)
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,recipe.color,products))
            stateChange = ADD_PRODUCT
            animPosition = products.size-1
            getProducts()
        }
    }



    fun cleanList(){
        CoroutineScope(Dispatchers.IO).launch {
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.clear()
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,recipe.color,products))
            stateChange = UPDATE_DATA
            getProducts()
            withContext(Dispatchers.Main){
                showToast(app.getString(R.string.product_list_cleared), getApplication())
            }
        }
    }

    fun pasteProducts(){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val products = mutableListOf<DataProduct>()
            val text: String = getClipboard(getApplication())
            if (text.isNotEmpty()){
                val strings = text.split("\n").toTypedArray()
                val recipe: DataRecipe = recipesDao.get(listId!!)
                products.addAll(recipe.products!!)
                for (name in strings) {
                    val product = DataProduct(name.trim(), currentTimeToLong(), false,"",null)
                    products.add(product)
                    animPosition = products.size
                }
                recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,recipe.color,products))
                stateChange =  UPDATE_DATA
                getProducts()
                withContext(Dispatchers.Main){
                    showToast(app.getString(R.string.products_added), getApplication())
                }
            }

        }

    }

    fun shareRecipe(context: Context, name: String){
        val list = productsList.value
            val sb = StringBuilder()

        sb.append(name)
        sb.append("\n")
        sb.append("\n")
        sb.append(recipeText.value.toString())
        sb.append("\n")
        sb.append("\n")
        sb.append("${context.getText(R.string.products)}:")
        sb.append("\n")
        sb.append("\n")
        if (list!!.isNotEmpty()){
                for (product in list) {
                    if (product.amount!=null&&product.amount!=""){
                        sb.append("${product.name} - ${product.amount}")
                    }else{
                        sb.append(product.name)
                    }

                    sb.append("\n")
                }

        }

        if (sb.toString().isNotEmpty()){
            shareText(sb.toString(),context)
        }
    }




    private  fun sortProducts(products: List<DataProduct>):List<DataProduct>{
        if (products.isNotEmpty()&&products.size!=1){
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

            return sortedList
        }else{
            return products
        }



    }





}