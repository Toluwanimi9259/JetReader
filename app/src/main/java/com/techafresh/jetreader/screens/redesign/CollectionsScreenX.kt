package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.util.bookToJson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CollectionsScreenX(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    var books = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            AppBarX(title = "Saved Books",/* icon = Icons.Default.ArrowBack, */ showProfile = false, navController = navController){
//                navController.navigate(AppScreensX.HomeScreenX.name){
//                    navController.popBackStack()
//                }
            }
        },
        bottomBar = {HomeNav(navController = navController)},
        contentColor = Color.White
    ) {
        Surface(modifier = Modifier.padding(it), color = Color.White) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!
            }else {
                emptyList()
            }

            if (viewModel.data.value.loading == true) {
                LinearProgressIndicator()
                 Log.d("Load", "ReaderStatsScreen: Loading....")
            }
            else {
                Log.d("Load", "ReaderStatsScreen: Done!!....")
                 Log.d("Sie", "ReaderStatsScreen: ${books}")
                Divider()
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    contentPadding = PaddingValues(16.dp)
                ) {

//                    filter by finished books
                    val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter {
                        // Supposed to filter sth here
                        (it.userId == currentUser?.uid)
                    }}else {
                        emptyList()
                    }
                    items(readBooks){
                        BookRow2(book = it, navController = navController){
                            val book = URLEncoder.encode(bookToJson(it), StandardCharsets.UTF_8.toString())
                            navController.navigate(AppScreensX.ReviewScreen.name +  "/${book}")
                        }
                    }
                }
            }
        }
    }

}