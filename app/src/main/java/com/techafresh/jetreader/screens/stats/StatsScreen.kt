package com.techafresh.jetreader.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.search.BookRow
import com.techafresh.jetreader.util.formatDate

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
){
    var books = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    Scaffold(
        topBar = {
            AppBarX(title = "Book Stats", icon = Icons.Default.ArrowBack, showProfile = false, navController = navController){
                navController.navigate(AppScreens.HomeScreen.name)
            }

        },)

    {
        Surface(modifier = Modifier.padding(it)) {
            //Only show books by this user that have been read
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
                    Text(text = "Hi, ${currentUser?.email.toString().split("@")[0].uppercase()}")
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = CircleShape,
                    elevation = cardElevation(5.dp),
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
                    // Log.d("Load", "ReaderStatsScreen: Loading....")
                }
                else {
                    //Log.d("Load", "ReaderStatsScreen: Done!!....")
                    // Log.d("Sie", "ReaderStatsScreen: ${filteredBooks.toString()}")
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentPadding = PaddingValues(16.dp)
                    ) {

                        //filter by finished books
                        val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                            viewModel.data.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }

                        }else {
                            emptyList()
                        }
                        items(readBooks){
                            BookRow(book = it, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookRow(
    book: MBook,
    navController: NavController,
) {

    Card(modifier = Modifier
        .clickable {
            //go to details screen and show more about the book
            // navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = cardElevation(6.dp)){

        Row( modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {
            var imageUrl = ""
//            val photoUrl = remember {
//                mutableStateOf(value = book.volumeInfo.imageLinks!!.smallThumbnail)
//            }
            imageUrl = if(book.photoUrl?.isEmpty() == true){
                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"

            }else {
                book.photoUrl!!
            }

            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween){

                    Text("${book.title}",
                        overflow = TextOverflow.Ellipsis)
                    if (book.rating!! > 3.0) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))//trick to move the icon to the end of the row!
                        Icon(Icons.Rounded.ThumbUp, contentDescription = "Thumbs up",
                            tint = Color.Red.copy(alpha = 0.5f))
                    }else {
                        Box {}

                    }

                }

                Text(" Author: ${book.authors.toString()}", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)

                Text("Started: ${formatDate(book.startedReading!!)}", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)
                Text("Finished: ${formatDate(book.finishedReading!!)}", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium)
//                Text(book.categories.toString(), softWrap = true,
//                    overflow = TextOverflow.Clip,
//                    fontStyle = FontStyle.Italic,
//                    style = MaterialTheme.typography.caption)

            }
        }
    }
}