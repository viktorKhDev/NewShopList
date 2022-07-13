package com.viktor.kh.dev.shoplist.utils

import android.text.Editable
import android.text.TextWatcher

class FollowText constructor(private val onSearchTextChange: OnSearchTextChange): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
       onSearchTextChange.textChange(s.toString())
    }

    interface OnSearchTextChange{
        fun textChange(s:String)
    }
}