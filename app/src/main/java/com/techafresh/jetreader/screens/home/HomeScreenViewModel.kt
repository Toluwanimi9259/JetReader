package com.techafresh.jetreader.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.repo.FireBaseRepo
import com.techafresh.jetreader.util.Wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repo : FireBaseRepo): ViewModel() {
    val data: MutableState<Wrapper<List<MBook>, Boolean, Exception>> = mutableStateOf(
        Wrapper(listOf(), true, Exception(""))
    )

    val data2 : MutableState<Wrapper<List<MBook>, Boolean, Exception>> = mutableStateOf(
        Wrapper(listOf(), true, Exception(""))
    )

    init {
        getAllBooksFromDatabase()
//        getCurrentUserBooks()
    }

    fun getAllBooksFromDatabase() {
    viewModelScope.launch {
        data.value.loading = true
        data.value = repo.getAllBooksFromDatabase()
        if (!data.value.data.isNullOrEmpty()) {
            data.value.loading = false
        }
//        Log.d("GET", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")
        }
    }

    fun getCurrentUserBooks(){
        viewModelScope.launch {
            data2.value.loading = true
            data2.value = repo.getAllCurrentUserBooks()
            if (!data2.value.data.isNullOrEmpty()){
                data2.value.loading = false
            }
        }
    }
}