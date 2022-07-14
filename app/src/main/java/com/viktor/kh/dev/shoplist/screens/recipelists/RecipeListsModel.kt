package com.viktor.kh.dev.shoplist.screens.recipelists

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import com.viktor.kh.dev.shoplist.utils.currentTimeToLong
import com.viktor.kh.dev.shoplist.utils.listId
import com.viktor.kh.dev.shoplist.utils.listName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListsModel @Inject constructor(application: Application) :AndroidViewModel(application) {

    @Inject lateinit var recipesDao: RecipesDao
    var initAnim = false
    var isAddClicked = false


   val dataRecipes : MutableLiveData <List<DataRecipe>> by lazy {
      MutableLiveData<List<DataRecipe>>().also {
          getRecipes()
      }

  }


    fun init(){
        isAddClicked = false
        initAnim = true
        getRecipes()
    }





    private fun getRecipes(){
        // get all recipes from DB
        CoroutineScope(Dispatchers.IO).launch {
            dataRecipes.postValue(recipesDao.getAll())
            Log.d("MyLog", dataRecipes.value?.size.toString())
            Log.d("MyLog", "getLists() in model")
        }

    }


    fun deleteRecipe(position : Int){
        //delete recipe on position
        initAnim = false
        isAddClicked = false
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.delete(dataRecipes.value!![position])
            getRecipes()
        }


    }

    fun addRecipe(name: String){
        //add recipe with name
        initAnim = false
        isAddClicked = true
        val listProduct :List<DataProduct> = emptyList()
        val productList = DataRecipe(0,name,"", currentTimeToLong(),listProduct)
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.insert(productList)
            getRecipes()
        }

    }

    fun setRecipe(position: Int,name: String){
        isAddClicked = false
        initAnim = false
        val list : DataRecipe = dataRecipes.value!![position]
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.update(
                DataRecipe(
                    list.id, name,list.text, list.date,list.products
                )
            )
            getRecipes()
        }
    }

    fun openRecipe(controller: NavController, dataRecipe: DataRecipe){
        // open recipe on position
        isAddClicked = false
        var bundle = Bundle()
        bundle.putInt(listId,dataRecipe.id)
        bundle.putString(listName,dataRecipe.name)
        controller.navigate(R.id.action_recipeListsFragment_to_recipeFragment,bundle)
    }



}