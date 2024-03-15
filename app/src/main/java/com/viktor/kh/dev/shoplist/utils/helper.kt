package com.viktor.kh.dev.shoplist.utils




import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.viktor.kh.dev.shoplist.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


val format = SimpleDateFormat("dd.MM.yyyy")
val formatForLog = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")







////preferences

var sortItems = ""
var sortLists = ""
//var sortByDate = false
var colorLists = true
var colorItems = true
var firstLaunchForProductsDB = false

//////////



const val LIST_ID = "ListId"
const val LIST_NAME = "ListName"
const val LIST_COLOR = "ListColor"
const val ADD_PRODUCT = 1
const val CHANGE_READY = 2
const val DELETE_PRODUCT = 3
const val UPDATE_DATA = 0
const val APP_PREF = "appPreferences"
const val FIRST_LAUNCH_FOR_PRODUCTS = "products_pref_launch"











 fun loadSetting(context: Context){
     val sp = PreferenceManager.getDefaultSharedPreferences(context)
    sortItems = sp.getString(context.getString(R.string.sorting_items),
        context.getString(R.string.color_time_pref_value))!!
     sortLists = sp.getString(context.getString(R.string.sorting_lists),"")!!
     colorLists = sp.getBoolean(context.getString(R.string.color_lists_pref),true)
    colorItems = sp.getBoolean(context.getString(R.string.color_items_pref),true)

}


fun isNightTheme(context:Context):Boolean{
 var state = false
    when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> state =  true
        Configuration.UI_MODE_NIGHT_NO -> state =  false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> state = false
    }

    return state
}


fun toBlackNavAndStatusBar(context: Context,activity: FragmentActivity) {
    if (isNightTheme(context)) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            activity.window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            val view = activity.window.decorView
            @Suppress("DEPRECATION")
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        }
    }
}





val colorsLight = listOf(
    R.color.card_blue,
    R.color.card_red,
    R.color.card_green,
    R.color.card_cyan,
    R.color.card_orange,
    R.color.card_violet,
    R.color.card_yellow,
    R.color.card_dark_blue,
    R.color.card_pink,
    R.color.card_red_variant,
    R.color.card_blue_variant

)

fun getColors():List<Int>{
    return colorsLight
}



/*fun getHexColors(context: Context): List<String>{
   val listHex = mutableListOf<String>()
    for (i in colorsLight){
        listHex.add(" #${}")
    }
}*/


var currentCardColor = 0





fun convertLongToTime(time: Long): String {
    val date = Date(time)
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}




class ColorId{

    companion object{
        fun get():Int{
            var number  = MyRandom.random(colorsLight.size - 1)
            return number
        }
    }


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





fun writeLog(log:String,context: Context,error:Boolean){
    try {
         val fileName = "log_" + formatForLog.format(Date(System.currentTimeMillis())) + ".txt"
        val fileNameWithError = "logcat_" + System.currentTimeMillis() + ".txt"
        val outputFile  = File(context.externalCacheDir,fileName)
        val outPutFileError = File(context.externalCacheDir,fileNameWithError)
        FileOutputStream(outputFile).use {
            it.write(log.toByteArray())
        }
        if (error){
            Runtime.getRuntime().exec("logcat -f " + outPutFileError.absolutePath)
        }

    }catch (e: Exception){

    }
}












