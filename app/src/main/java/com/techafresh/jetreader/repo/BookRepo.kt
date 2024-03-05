package com.techafresh.jetreader.repo

import android.util.Log
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.network.BooksApi
import com.techafresh.jetreader.util.Resource
import com.techafresh.jetreader.util.Wrapper
import javax.inject.Inject

class BookRepo @Inject constructor(private val api: BooksApi){
    private val listOfBooks = Wrapper<List<Item>, Boolean, Exception>()
    private val bookInfo = Wrapper<Item , Boolean, Exception>()

    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            val itemList = api.getAllBooks(searchQuery).items
            Log.d("ItemList", "success ${itemList.size}")
            Resource.Success(data = itemList)
        }catch (exception:Exception){
            Log.d("Book Repo ","failure ${exception.message}")
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBook(bookId)
        }catch (e: Exception){
            return Resource.Error("An error occurred")
        }
        Resource.Loading(data = false)
        return Resource.Success(response)

    }


}