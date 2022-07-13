package com.viktor.kh.dev.shoplist.utils

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


val format = SimpleDateFormat("dd.MM.yyyy")
const val true1 :String = "1"
const val falce0 :String = "0"
const val sortByName = "sort_by_name"
const val listId = "ListId"
const val listName = "ListName"
const val addProduct = 1
const val changeReady = 2
const val deleteProduct = 3
const val updateData = 0

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


fun getClipboard(context: Context): String {
    val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var pasteData = ""

    // If it does contain data, decide if you can handle the data.
    if (!clipboard.hasPrimaryClip()) {
    } else if (!clipboard.primaryClipDescription?.hasMimeType(
            MIMETYPE_TEXT_PLAIN
        )!!
    ) {

        // since the clipboard has data but it is not plain text
    } else {

        //since the clipboard contains plain text.
        val item: ClipData.Item = clipboard.primaryClip!!.getItemAt(0)

        // Gets the clipboard as text.
        pasteData = item.text.toString()
    }
    return pasteData
}


fun shareText(text: String?, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = "text/plain"
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }

}












