package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viktor.kh.dev.shoplist.repository.Repository
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductLists
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListsModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var repository: Repository

   val dataLists : MutableLiveData <DataProductLists> by lazy {
       MutableLiveData<DataProductLists>().also {
           getLists()
       }
   }


    fun getLists(): MutableLiveData<List<DataProductLists>> {
       return repository.lists
    }

}