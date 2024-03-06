package com.techafresh.jetreader.network

import com.techafresh.jetreader.model.Book
import com.techafresh.jetreader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {

    @GET("volumes")
    suspend fun getAllBooks(
        @Query("q")
        query: String,

        @Query("maxResults")
        maxResults: Int = 40
    ) : Book

    @GET("volumes/{bookId}")
    suspend fun getBook(@Path("bookId") bookId : String) : Item
}