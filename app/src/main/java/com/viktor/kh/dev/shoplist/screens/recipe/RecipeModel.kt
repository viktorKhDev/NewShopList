package com.viktor.kh.dev.shoplist.screens.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    val recipeText : MutableLiveData<String>  by lazy {
        MutableLiveData<String>().also {
            getText()
        }
    }


    fun initText(id: Int){
        if (listId!=id) {
            listId = id
           getText()
        }
    }





    fun initProducts(id: Int){
        stateChange = updateData
        animPosition = -1
        initAnim = true
        if (listId!=id) {
            listId = id
            getProducts()
        }

        Log.d("MyLog", "productsModel init with id ${id.toString()}")
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
            val newData  = DataRecipe(data.id,data.name,text,data.date,
                data.products?.let { sortProducts(it) })
            recipesDao.update(newData)
            withContext(Dispatchers.Main){
              recipeText.value = newData.text
            }
            Log.d("MyLog", "recipeModel get list")
        }
    }


    private fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            val data : DataRecipe = recipesDao.get(listId!!)
            val newData  = DataRecipe(data.id,data.name,data.text,data.date,
                data.products?.let { sortProducts(it) })
            recipesDao.update(newData)
            withContext(Dispatchers.Main){
                productsList.value = newData.products
            }
            Log.d("MyLog", "recipeModel get list")
        }
    }

    fun renameProduct(position: Int,name: String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val currentProduct = productsList.value!![position]
            val newProduct = DataProduct(name,currentProduct.date, falce0)
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.removeAt(position)
            products.add(newProduct)
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,products))
            stateChange = updateData
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
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,products))
            stateChange = deleteProduct
            animPosition = position
            getProducts()
        }
    }

    fun addProduct(productName: String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            val recipe: DataRecipe = recipesDao.get(listId!!)
            val dataProduct = DataProduct(productName.trim(), currentTimeToLong().toString(), falce0)
            val products  = mutableListOf<DataProduct>()
            recipe.products?.let { products.addAll(it) }
            products.add(dataProduct)
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,products))
            stateChange = addProduct
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
            recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,products))
            stateChange = updateData
            getProducts()

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
                    val product = DataProduct(name.trim(), currentTimeToLong().toString(), falce0)
                    products.add(product)
                    animPosition = products.size
                }
                recipesDao.update(DataRecipe(recipe.id,recipe.name,recipe.text,recipe.date,products))
                stateChange =  updateData
                getProducts()
            }

        }

    }

    fun shareRecipe(context: Context){

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