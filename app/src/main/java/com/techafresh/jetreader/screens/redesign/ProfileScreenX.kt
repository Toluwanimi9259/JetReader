package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel

@Composable
fun ProfileScreenX(navController: NavController,viewModel: HomeScreenViewModel = hiltViewModel()){
    var books = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            AppBarX(title = "Profile", icon = Icons.Default.ArrowBack, showProfile = false, navController = navController){
                navController.navigate(AppScreensX.HomeScreenX.name){
                    navController.popBackStack()
                }
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {

            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid) /*&& (mBook.finishedReading != null)*/
                }
            }else {
                emptyList()
            }

            Column {
                Row() {
                    Box(modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)) {
                        Icons.Sharp.Person
                    }
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0]}")
//                    Text(text = "Hi James")
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {

                    val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }

                    }else {
                        emptyList()
                    }

                    val readingBooks = books.filter { mBook ->
                        (mBook.startedReading != null && mBook.finishedReading == null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.titleSmall)
                        Divider()
                        Text(text = "You're reading : ${readingBooks.size} books")
                        Text(text = "You've read: ${readBooksList.size} books")
                    }
                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                    Log.d("Load", "ProfileScreen: Loading....")
                }
                else {
                    Log.d("Load", "ProfileScreen: Done!!....")
//                Log.d("Sie", "ProfileScreen: ${books}")
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
                                (it.userId == currentUser?.uid)&& (it.finishedReading != null)
                            }}else {
                            emptyList()
                        }
                        items(readBooks){
                            BookRow2(book = it, navController = navController){
//                            val book = URLEncoder.encode(bookToJson(it), StandardCharsets.UTF_8.toString())
//                            navController.navigate(AppScreensX.ReviewScreen.name +  "/${book}")
                            }
                        }
                    }
                }
            }
        }
    }
}