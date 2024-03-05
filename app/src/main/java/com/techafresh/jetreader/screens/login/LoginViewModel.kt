package com.techafresh.jetreader.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.techafresh.jetreader.model.MUser
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth : FirebaseAuth = Firebase.auth
    private val loading = MutableLiveData(false)


    fun logInWithEmailAndPassword(email : String , password : String , goToHome: () -> Unit) = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        goToHome()
                        Log.d("TAG", "logInWithEmailAndPassword: sucessfull")
                    }else{
                        Log.d("TAG", "Login task: ${it.exception?.message}")
                    }
                }
        }catch (ex : Exception){
            Log.d("TAG", "signInWithEmailAndPassword: ${ex.message}")
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            userId  = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is Great",
            profession = "Android Dev",
            id = null
        ).toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }

    fun signUpWithEmailAndPassword(email: String, password: String, goToHome: () -> Unit) = viewModelScope.launch {
        if (loading.value == false){
            loading.value = true
            auth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val displayName = it.result.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        goToHome()
                        Log.d("TAG", "signUpWithEmailAndPassword: sucessfull")
                    }else{
                        Log.d("TAG", "sign up task: ${it.exception?.message}")
                    }
                }
        }
    }
}