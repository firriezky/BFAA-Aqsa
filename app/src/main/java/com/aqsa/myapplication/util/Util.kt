package com.aqsa.myapplication.util

import android.content.Context
import android.widget.Toast

object Util {

    fun Context.makeLongToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
    }
}