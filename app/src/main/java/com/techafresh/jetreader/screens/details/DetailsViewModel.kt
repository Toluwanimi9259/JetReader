package com.techafresh.jetreader.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.repo.BookRepo
import com.techafresh.jetreader.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repo: BookRepo): ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repo.getBookInfo(bookId)
    }
}