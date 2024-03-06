package com.techafresh.jetreader.util

import com.google.gson.Gson
import com.techafresh.jetreader.model.MBook

fun bookToJson(book : MBook): String {
    val gson = Gson()
    return gson.toJson(book)
}

fun jsonToBook(json: String): MBook {
    val gson = Gson()
    return gson.fromJson(json, MBook::class.java)
}

fun main() {
    val book = MBook(
        title = "Title",
        authors = "Authors",
        description = "Description",
        categories = "Categories",
        notes = "Hello Nigga",
        photoUrl = "https://www.google.com",
        publishedDate = "2017-18-12",
        pageCount = "203",
        googleBookId = "qeeirjdddd",
        userId = "tolu",
    )

    println(bookToJson(book))
    println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ")
    println(jsonToBook((bookToJson(book))))
}