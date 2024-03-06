package com.viktor.kh.dev.shoplist.screens.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.repository.db.room.ProductsDao
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
    @Inject lateinit var productsDao: ProductsDao

    private var listId :Int? = null

    private val app = application
    //variable for check start animation
    private var initAnim = false
    var stateChange = UPDATE_DATA
    private var animPosition = -1
    var currentColor: Int? = null

    //need get from settings



    val productsList : MutableLiveData<List<ProductData>> by lazy {
        MutableLiveData<List<ProductData>>().also {
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
            val newData  = DataRecipe(data.id,data.name,text,data.date, data.color)
            recipesDao.update(newData)
            withContext(Dispatchers.Main){
              recipeText.value = newData.text
            }
        }
    }


    fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            val data = productsDao.getProductsForList(listId!!,true)
            withContext(Dispatchers.Main){
                productsList.value = sortProducts(data)
            }
        }
    }

    fun renameProduct(position: Int,name: String,amount: String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {
            var currentProduct = productsList.value!![position]

            val newProduct = ProductData(currentProduct.id,name,currentProduct.date, false,amount,
                null,
                listId,
                true
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

    fun addProduct(productName: String,amount :String){
        initAnim = false
        CoroutineScope(Dispatchers.IO).launch {

            productsDao.insert(ProductData(
                0,
                productName.trim(),
                currentTimeToLong(),
                false,
                amount,
                null,
                listId!!,
                true
            ))

            stateChange = ADD_PRODUCT
            productsList.value?.let { animPosition = it.size }
            getProducts()
        }
    }



    fun cleanList(){
        CoroutineScope(Dispatchers.IO).launch {

            productsDao.deleteProductsForList(listId!!,true)
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
            //val products = mutableListOf<DataProduct>()
            val text: String = getClipboard(getApplication())
            if (text.isNotEmpty()){
                val strings = text.split("\n").toTypedArray()
                /*val recipe: DataRecipe = recipesDao.get(listId!!)
                products.addAll(recipe.products!!)*/
                val copyList  = mutableListOf<ProductData>()
                for (name in strings) {
                    if (name.isNotEmpty()){
                        val  product = ProductData(
                            0,
                            name,
                            currentTimeToLong(),
                            false,
                            "",
                            null,
                            listId!!,
                            true
                        )
                        copyList.add(product)
                    }

                    //animPosition = products.size

                }

                productsDao.addProducts(copyList)
                stateChange =  UPDATE_DATA
                getProducts()
                productsList.value?.let { animPosition = it.size }
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




    private  fun sortProducts(products: List<ProductData>):List<ProductData>{
        if (products.isNotEmpty()&&products.size!=1) {
            return products.sortedWith(compareBy { it.date })
        }else{
            return products
        }



    }





}