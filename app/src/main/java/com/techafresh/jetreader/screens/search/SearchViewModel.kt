package com.techafresh.jetreader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.repo.BookRepo
import com.techafresh.jetreader.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo : BookRepo) : ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading = mutableStateOf(true)

    var fictionList: List<Item> by mutableStateOf(listOf())
    var fictionIsLoading = mutableStateOf(true)

    var horrorList: List<Item> by mutableStateOf(listOf())
    var horrorIsLoading = mutableStateOf(true)

    var mysteryList: List<Item> by mutableStateOf(listOf())
    var mysteryIsLoading = mutableStateOf(true)

    var adventureList: List<Item> by mutableStateOf(listOf())
    var adventureIsLoading = mutableStateOf(true)

    var fantasyList: List<Item> by mutableStateOf(listOf())
    var fantasyIsLoading = mutableStateOf(true)

    var thrillerList: List<Item> by mutableStateOf(listOf())
    var thrillerIsLoading = mutableStateOf(true)

    var dystopianList: List<Item> by mutableStateOf(listOf())
    var dystopianIsLoading = mutableStateOf(true)

    var biographyList: List<Item> by mutableStateOf(listOf())
    var biographyIsLoading = mutableStateOf(true)

    var cookingList: List<Item> by mutableStateOf(listOf())
    var cookingIsLoading = mutableStateOf(true)

    var selfHelpList: List<Item> by mutableStateOf(listOf())
    var selfHelpIsLoading = mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchAdventure("Romance")
        searchFiction("Fiction")
        searchHorror("Horror")
        searchMystery("Mystery")
        searchSelfHelp("selfhelp")
        searchBiography("Biography")
        searchDystopian("Dystopian")
        searchCooking("Cooking")
        searchFantasy("Fantasy")
        searchThriller("Thriller")
        searchBooks("Science Fiction")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchBooks: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        Log.d("GNCC", "searchBooks: Size = ${list.size}")
                        list = response.data!!
                        if (list.isNotEmpty()) isLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }
    }

    private fun searchFiction(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchFiction: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {

                        fictionList = response.data!!
                        Log.d("GNCC", "searchFiction: Size = ${fictionList.size}")
                        if (fictionList.isNotEmpty()) fictionIsLoading.value = false

                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                Log.d("Search Fiction", "searchBooks: ${e.localizedMessage}")
            }
        }

    }

    private fun searchHorror(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchHorror: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        horrorList = response.data!!
                        Log.d("GNCC", "searchHorror: Size = ${horrorList.size}")
                        if (horrorList.isNotEmpty()) horrorIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }

    private fun searchMystery(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchMystery: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        mysteryList = response.data!!
                        Log.d("GNCC", "searchMystery: Size = ${mysteryList.size}")
                        if (mysteryList.isNotEmpty()) mysteryIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }

    private fun searchAdventure(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchADv: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        adventureList = response.data!!
                        Log.d("GNCC", "searchAdv: Size = ${adventureList.size}")
                        if (adventureList.isNotEmpty()) adventureIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }

    private fun searchBiography(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchBio: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        biographyList = response.data!!
                        Log.d("GNCC", "searchBio: Size = ${biographyList.size}")
                        if (biographyList.isNotEmpty()) biographyIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }
    private fun searchCooking(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchCooking: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        cookingList = response.data!!
                        Log.d(
                            "Search Cooking",
                            "fetchBooks: Success ${response.data?.get(0)?.volumeInfo}"
                        )
                        if (cookingList.isNotEmpty()) cookingIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }
    private fun searchDystopian(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchDystopian: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        dystopianList = response.data!!
                        Log.d("GNCC", "searchDyst: Size = ${dystopianList.size}")
                        if (dystopianList.isNotEmpty()) dystopianIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }
    private fun searchThriller(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchThriller: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        thrillerList = response.data!!
                        Log.d("GNCC", "searchThriller: Size = ${thrillerList.size}")
                        if (thrillerList.isNotEmpty()) thrillerIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }
    private fun searchFantasy(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                Log.d("GNCC", "searchFantasy: Running")
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        fantasyList = response.data!!
                        Log.d("GNCC", "searchFantasy: Size = ${fantasyList.size}")
                        if (fantasyList.isNotEmpty()) fantasyIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }
    private fun searchSelfHelp(query: String){
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            try {
                when (val response = repo.getBooks(query)) {
                    is Resource.Success -> {
                        selfHelpList = response.data!!
                        Log.d(
                            "Search SelfHelp",
                            "fetchBooks: Success ${response.data?.get(0)?.volumeInfo}"
                        )
                        if (selfHelpList.isNotEmpty()) selfHelpIsLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "fetchBooks: Failure ${response.data.toString()}")
                    }

                    else -> {}
                }

            } catch (e: Exception) {
                Log.d("Exce", "searchBooks: ${e.localizedMessage}")
            }
        }

    }

}
