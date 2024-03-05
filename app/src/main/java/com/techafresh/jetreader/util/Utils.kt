package com.techafresh.jetreader.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp

fun showToast(context: Context, msg:String){
    Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
}
fun formatDate(timestamp: Timestamp): String {
    val date = android.icu.text.DateFormat.getDateInstance().format(timestamp.toDate()).toString().split(",")[0] //omit the year :)
    Log.d("TAG", "formatDate: $date")
    return date
}