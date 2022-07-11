package com.viktor.kh.dev.shoplist.helpers

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.viktor.kh.dev.shoplist.R


fun AndroidViewModel.showToast(text:String,context: Context){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(text:String,context: Context?){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}

