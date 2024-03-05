package com.techafresh.jetreader.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.util.Wrapper
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseRepo @Inject constructor(private val queryBook : Query) {
    suspend fun getAllBooksFromDatabase(): Wrapper<List<MBook>, Boolean, Exception> {

        val dataOrException = Wrapper<List<MBook>, Boolean, Exception>()

        try {

            dataOrException.loading = true
            //In order to get the await, we need to add the: implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.x" to gradle
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
            // Log.d("Inside", "getAllBooksFromDatabase: ${dataOrException.data?.toList()}")
        }catch (e: FirebaseFirestoreException){
            dataOrException.e = e
        }
        return dataOrException

    }

    suspend fun getAllCurrentUserBooks() : Wrapper<List<MBook>, Boolean, Exception>{
        val dataOrException = Wrapper<List<MBook>, Boolean, Exception>()
        val currentUserId = FirebaseAuth.getInstance().currentUser

        try {
            dataOrException.loading = true
            val dd = FireBaseRepo(FirebaseFirestore.getInstance().collection("books").whereEqualTo("user_id", currentUserId))
            dataOrException.data = dd.queryBook.get().await().documents.map {
                it.toObject(MBook::class.java)!!
            }
        }catch (e: FirebaseFirestoreException){
            dataOrException.e = e
        }
        return dataOrException
    }
}