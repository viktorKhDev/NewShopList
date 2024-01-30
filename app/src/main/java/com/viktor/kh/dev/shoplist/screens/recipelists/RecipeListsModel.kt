package com.viktor.kh.dev.shoplist.screens.recipelists

import android.app.Application
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.ProductsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListsModel @Inject constructor(application: Application) :AndroidViewModel(application) {

    @Inject lateinit var recipesDao: RecipesDao
    //
    @Inject lateinit var productsDao: ProductsDao
    //
    var initAnim = false
    var isAddClicked = false
    var currentColor: Int? = null
    private val app = application

   val dataRecipes : MutableLiveData <List<DataRecipe>> by lazy {
      MutableLiveData<List<DataRecipe>>().also {
          getRecipes()
      }

  }

    var dataForSearch = mutableListOf<DataRecipe>()


    val dataColors :MutableLiveData<ColorClickedList> by lazy {
        MutableLiveData<ColorClickedList>()
    }


    fun init(){
        isAddClicked = false
        initAnim = true
        getRecipes()
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

    private fun getRecipes(){
        // get all recipes from DB
        CoroutineScope(Dispatchers.IO).launch {

            dataRecipes.postValue(recipesDao.getAll())
        }

    }


    fun deleteRecipe(position : Int){
        //delete recipe on position
        initAnim = false
        isAddClicked = false
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.delete(dataRecipes.value!![position])
            //
            productsDao.deleteProductsForList(dataRecipes.value!![position].id,true)
            //
            getRecipes()
        }


    }

    fun addRecipe(name: String){
        //add recipe with name
        initAnim = false
        isAddClicked = true
        val listProduct :List<DataProduct> = emptyList()
        val productList = DataRecipe(0,name,"", currentTimeToLong(),currentColor)
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.insert(productList)
            getRecipes()
        }

    }

    fun setRecipe(position: Int,name: String){
        //set name and color for recipe
        isAddClicked = false
        initAnim = false
        val list : DataRecipe = dataRecipes.value!![position]
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.update(
                DataRecipe(
                    list.id, name,list.text, list.date, currentColor)
            )
            getRecipes()
        }
    }

    fun openRecipe(controller: NavController, dataRecipe: DataRecipe){
        // open recipe on position
        isAddClicked = false
        val bundle = Bundle()
        bundle.putInt(LIST_ID,dataRecipe.id)
        bundle.putString(LIST_NAME,dataRecipe.name)
        if (dataRecipe.color!=null){
            bundle.putInt(LIST_COLOR, ContextCompat.getColor(getApplication(), dataRecipe.color))
        }else{
            bundle.putInt(LIST_COLOR,0)
        }
        controller.navigate(R.id.action_recipeListsFragment_to_recipeFragment,bundle)
    }

    fun searchData(list: List<DataRecipe>){
        dataRecipes.value = list
    }

    fun clearSearchData(){
        getRecipes()
        dataForSearch.clear()
    }


    fun setSearchData(){
        dataRecipes.value?.let { dataForSearch.addAll(it) }
    }



}