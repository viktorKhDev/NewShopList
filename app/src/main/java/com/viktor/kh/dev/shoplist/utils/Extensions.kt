package com.viktor.kh.dev.shoplist.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel


fun AndroidViewModel.showToast(text:String,context: Context){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(text:String,context: Context?){
 Toast.makeText(context,text,Toast.LENGTH_LONG).show()
}


fun EditText.showKeyboard(
) {
 requestFocus()
 val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
         InputMethodManager
 imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard(
) {
 val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
         InputMethodManager
 imm.hideSoftInputFromWindow(this.windowToken, 0)
}


