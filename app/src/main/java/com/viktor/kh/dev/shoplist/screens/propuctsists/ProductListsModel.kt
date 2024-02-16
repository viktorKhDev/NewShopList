package com.viktor.kh.dev.shoplist.screens.propuctsists


import android.app.Application
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.repository.db.room.ProductListsDao
import com.viktor.kh.dev.shoplist.repository.db.room.ProductsDao
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDao
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import javax.inject.Inject


@HiltViewModel
class ProductListsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var productListsDao: ProductListsDao

    ////
    @Inject lateinit var recipesDao: RecipesDao
    @Inject lateinit var productsDao: ProductsDao
    ///




    private val app = application
    //variable for check start animation
    var initAnim = false
    var isAddClicked = false
    var currentColor: Int? = null

    val dataColors :MutableLiveData<ColorClickedList> by lazy {
       MutableLiveData<ColorClickedList>()
   }

   val dataLists : MutableLiveData <List<DataProductList>> by lazy {
       MutableLiveData <List<DataProductList>>().also {
           getLists()
       }
   }

    var dataForSearch = mutableListOf<DataProductList>()

    fun init(){
        isAddClicked = false
        initAnim = true
        getLists()
        dataColors.value = ColorClickedList()
        //copyProductsToProductsDB()

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




    private fun getLists(){
        // get all list from DB
       CoroutineScope(Dispatchers.IO).launch {
           val tempList = productListsDao.getAll()
           val tempList2 = mutableListOf<DataProductList>()
           for (i in tempList){
               if (!colorsLight.contains(i.color)){
                   val newList = DataProductList(i.id,i.name,i.date,null)
                   tempList2.add(newList)
               }else{
                   tempList2.add(i)
               }
           }
           dataLists.postValue(tempList2)
       }

    }


    fun deleteList(position : Int){
        //delete list on position
        initAnim = false
        isAddClicked = false
        CoroutineScope(Dispatchers.IO).launch {
                productListsDao.delete(dataLists.value!![position])
            ///
                productsDao.deleteProductsForList(dataLists.value!![position].id,false)
            ///
            getLists()
        }


    }

    fun addList(name: String){
        //add list with name
        initAnim = false
        isAddClicked = true
        val productList = DataProductList(0,name, currentTimeToLong(),currentColor)
        CoroutineScope(Dispatchers.IO).launch {
            productListsDao.insert(productList)
            getLists()
        }

    }

    fun setList(position: Int,name: String){
        isAddClicked = false
        initAnim = false
        val list : DataProductList = dataLists.value!![position]
        CoroutineScope(Dispatchers.IO).launch {
         productListsDao.update(
             DataProductList(
             list.id, name, list.date,currentColor
         )
         )
           getLists()
      }
    }

    fun openList(controller: NavController, dataProductList: DataProductList){
        // open list on position
        isAddClicked = false
        var bundle = Bundle()
       bundle.putInt(LIST_ID,dataProductList.id)
        bundle.putString(LIST_NAME,dataProductList.name)
        if (dataProductList.color!=null){
            try {
                bundle.putInt(LIST_COLOR, ContextCompat.getColor(getApplication(), dataProductList.color))
            }catch (e: NotFoundException){
                bundle.putInt(LIST_COLOR,0)
                writeLog("error put color in openList : ${e.printStackTrace()}",
                    app.applicationContext,true)
            }

        }else{
            bundle.putInt(LIST_COLOR,0)
        }
        controller.navigate(R.id.action_productListsFragment_to_productsFragment,bundle)
    }




    fun searchData(list: List<DataProductList>){
        dataLists.value = list
    }

    fun clearSearchData(){
        getLists()
        dataForSearch.clear()
    }


    fun setSearchData(){
        dataLists.value?.let { dataForSearch.addAll(it) }
    }

}

