package com.viktor.kh.dev.shoplist.helpers

import android.content.Context
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel


fun AndroidViewModel.showToast(text:String,context: Context){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(text:String,context: Context?){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}