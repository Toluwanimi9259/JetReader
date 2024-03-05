package com.techafresh.jetreader.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.techafresh.jetreader.components.InputField
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.screens.home.AppBarX


@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            AppBarX(
                title = "Search Books",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
            ){
                navController.navigate(AppScreens.HomeScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                SearchX(
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ){searchQuery ->
                   viewModel.searchBooks(searchQuery)
                }
                BookList(navController = navController)
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchX(
    modifier: Modifier = Modifier,
    loading : Boolean = false,
    hint : String = "Search",
    onSearch : (String) -> Unit
){
    Column {
        val searchQueryState  = rememberSaveable { mutableStateOf("") }
        val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }
        val keyboardController = LocalSoftwareKeyboardController.current
        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions{
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
        
        
    }

}

@Composable
fun BookList(
    navController: NavController,
    viewModel:SearchViewModel = hiltViewModel(),
) {

    if (viewModel.isLoading.value){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = viewModel.list.filter {
                it.volumeInfo.imageLinks.smallThumbnail != null
            }) { item ->
                BookRow(book = item, navController = navController)
            }
        }
    }
}

@Composable
fun BookRow(
    book: Item,
    navController: NavController,
) {

    Card(modifier = Modifier
        .clickable {
            //go to details screen and show more about the book
            navController.navigate(AppScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = cardElevation(6.dp)){
        Row( modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {
            Image(
                painter = rememberAsyncImagePainter(
                   if (book.volumeInfo.imageLinks.smallThumbnail.isNullOrEmpty()) "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80" else book.volumeInfo.imageLinks.smallThumbnail
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            Column {

                Text(
                    book.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis)

                Text(" Author: ${
                    if (book.volumeInfo.authors[0] == null) "Authors" else book.volumeInfo.authors[0] 
                }", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

                Text("Date: ${book.volumeInfo.publishedDate}", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

                Text(
                    if (book.volumeInfo.categories == null) "Categories"   else book.volumeInfo.categories.toString(), softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

            }
        }
    }
}