package com.techafresh.jetreader.di

import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.network.BooksApi
import com.techafresh.jetreader.repo.FireBaseRepo
import com.techafresh.jetreader.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository(
    ) = FireBaseRepo(queryBook = FirebaseFirestore.getInstance()
        .collection("books")
    )

    @Provides
    @Singleton
    fun provideBookApi() : BooksApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BooksApi::class.java)
    }
}