package com.viktor.kh.dev.shoplist.helpers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.viktor.kh.dev.shoplist.screens.main.MainActivity
import dagger.hilt.android.internal.Contexts
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val format = SimpleDateFormat("dd.MM.yyyy")

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    return format.parse(date).time
}

fun initFocusAndShowKeyboard (et :EditText, activity: AppCompatActivity){
    et.requestFocus()
    et.postDelayed(kotlinx.coroutines.Runnable {
        val  inputMethodManager :InputMethodManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(et,InputMethodManager.SHOW_IMPLICIT)
    },0)
}


fun cancelKeyboard(view: View,activity: AppCompatActivity){
    val  inputMethodManager :InputMethodManager = activity
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
}


