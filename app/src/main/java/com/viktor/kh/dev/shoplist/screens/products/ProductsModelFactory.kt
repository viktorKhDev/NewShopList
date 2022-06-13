package com.viktor.kh.dev.shoplist.screens.products

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductsModelFactory(val application: Application, val listId: Int)
    : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsModel(application, listId) as T
    }
}