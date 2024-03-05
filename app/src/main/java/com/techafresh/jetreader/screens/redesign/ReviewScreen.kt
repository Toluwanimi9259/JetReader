package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.R
import com.techafresh.jetreader.components.InputField
import com.techafresh.jetreader.components.RatingBar
import com.techafresh.jetreader.components.RoundedButton
import com.techafresh.jetreader.components.SimpleForm
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.update.ShowSimpleForm
import com.techafresh.jetreader.util.Wrapper
import com.techafresh.jetreader.util.formatDate
import com.techafresh.jetreader.util.jsonToBook
import com.techafresh.jetreader.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReviewScreen(
    navController: NavController,
    book : String,
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            AppBarX(
                title = "Review",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {

        val bookInfo = produceState<Wrapper<List<MBook>, Boolean, Exception>>(
            initialValue = Wrapper(data = emptyList(), true, Exception(""))
        ) {
            value = viewModel.data.value
        }.value


        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                val bookString = book.replace('+',' ')
                val book : MBook = jsonToBook(bookString)
                Log.d("TAG", "ReviewScreen: Book = $book")

                val review  = remember{ mutableStateOf("") } // Note

                val context = LocalContext.current

                val isStartedReading = remember{
                    mutableStateOf(false)
                }

                val isFinishedReading = remember{
                    mutableStateOf(false)
                }

                val ratingVal = remember {
                    mutableIntStateOf(0)
                }


                // Components

                // Top Card
                ShowBook(book)

                // TextField
                SimpleForm(
                    hint = "What do you think about the book?",
                    defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString() else ""
                ){
                    review.value = it
                }

                // Two TextButtons
                Row(modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start) {

                    TextButton(onClick = {
                        isStartedReading.value = true
                    }, enabled = book.startedReading == null) {
                        if (book.startedReading == null){
                            if (!isStartedReading.value){

                                Text(text =  "Start Reading")
                            }else {

                                Text(text = "Started reading!",
                                    modifier = Modifier.alpha(0.6f),
                                    color = Color.Red.copy(alpha = 0.5f)
                                )
                            }
                        }else{
                            Text(text = "Started on: ${formatDate(book.startedReading!!)}")
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    TextButton(onClick = {
                        isFinishedReading.value = true
                    }, enabled =  book.finishedReading == null) {
                        if (book.finishedReading == null) {
                            if (!isFinishedReading.value){
                                Text(text = "Mark as Read")
                            }else {
                                Text(text = "Finished Reading!")
                            }
                        }else {
                            Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")
                        }

                    }



                }

                // Rating Bar
                Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
                book.rating?.toInt()?.let {
                    RatingBar(rating = it){rating ->
                        ratingVal.intValue = rating
                        Log.d("Rating", "ShowSimpleForm: $rating")
                    }
                }

                Spacer(modifier = Modifier.padding(bottom = 15.dp))

                Row {

                    //Only update if the fields have new data!
                    val changedNotes = book.notes != review.value
                    val changedRating = book.rating?.toInt() != ratingVal.value

                    val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
                    val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading

                    val bookUpdate = changedNotes || changedRating || isStartedReading.value  || isFinishedReading.value

                    val bookToUpdate = hashMapOf(
                        "finished_reading_at" to isFinishedTimeStamp,
                        "started_reading_at" to isStartedTimeStamp,
                        "rating" to ratingVal.value,
                        "notes" to review.value).toMap()

                    // Update
                    RoundedButton(label = "Update"){
                        if (bookUpdate) {
                            FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                                .update(bookToUpdate).addOnCompleteListener {
                                    showToast(context,"Book Updated Successfully!")
                                    navController.navigate(AppScreensX.HomeScreenX.name){
                                        navController.popBackStack()
                                    }
                                    Log.d("Update", "ShowSimpleForm: ${it.result.toString()}")
                                }.addOnFailureListener {
                                    Log.w("TAG", "Error updating document", it)
                                }
                        }else {
                            Log.d("TAG", "ReviewScreen: Change Something nigga")
                        }
//                       / save or update
                    }

                    Spacer(modifier = Modifier.width(100.dp))

                    // Delete
                    RoundedButton(label = "Delete"){
//                        val don = false
//                        if (don)
                            CoroutineScope(Dispatchers.IO).launch {
                                Log.d("TAG", "ShowSimpleForm: About To Delete Book id = ${book.id}")
                                FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                                    .delete()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful){
                                            navController.navigate(AppScreensX.HomeScreenX.name){
                                                navController.popBackStack()
                                            }
                                        }
                                    }.addOnFailureListener{
                                        Log.d("TAG", "Delete Problem: $it")
                                    }
                            }
//                        else
                            Log.d("TAG", "ReviewScreen: Delete Pressed")
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ShowBook(
    book : MBook = MBook(title = "Starting With Android", authors = "[Dr.M.M Sharma]", publishedDate = "2024-07-03",
        description = "This book is written to cover all the aspects of Android in a comprehensive way. Apart from the basics of Android, this book covers its various features like tools for development of app and applications of Android platform. It teaches everything you will need to know to successfully develop your own Android applications. The book addresses all the fundamentals including Intents, Activities user interfaces, SMS messaging, databases.It explains how to adapt to display orientation,"),
//    onCardPress: (String) -> Unit
){
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
//                onCardPress(book.id.toString())
            },
        elevation = cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .height(130.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {

                val interFamily = FontFamily(
                    Font(R.font.inter_light, FontWeight.Light),
                    Font(R.font.inter_regular, FontWeight.Normal),
                    Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
                    Font(R.font.inter_medium, FontWeight.Medium),
                    Font(R.font.inter_bold, FontWeight.Bold),
                    Font(R.font.inter_black, FontWeight.Black)
                )


                Surface(
                    modifier = Modifier
                        .height(132.dp)
                        .width(90.dp), shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                ) {
                    AsyncImage(
                        contentScale = ContentScale.FillBounds,
                        model = book.photoUrl,
                        contentDescription = null
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .fillMaxWidth(1f),
                        text = if (book.title.isNullOrEmpty()) "Description" else book.title.toString(),
                        softWrap = true,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = interFamily,
                        color = Color(0xFF0D0842),
                        maxLines = 1,
                    )


                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 2.dp, end = 2.dp)
                            .fillMaxWidth(1f),
                        text = if (book.authors.isNullOrEmpty()) "Authors" else book.authors.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = interFamily,
                        color = Color(0xFF0D0842).copy(0.8f),
                        maxLines = 1,
                    )

                    val descriptionText = HtmlCompat.fromHtml(
                        book.description.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString()

                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 2.dp, end = 2.dp)
                            .fillMaxWidth(1f),
                        text = if (book.description.isNullOrEmpty()) "Publish Date" else descriptionText,
                        softWrap = true,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = interFamily,
                        color = Color(0xFF0D0842).copy(0.52f),
                        maxLines = 2,
                    )

                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(top = 5.dp, end = 2.dp)
                            .fillMaxWidth(1f),
                        text = if (book.publishedDate.isNullOrEmpty()) "Publish Date" else book.publishedDate!!,
                        softWrap = true,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = interFamily,
                        color = Color(0xFF0D0842).copy(0.52f),
                        maxLines = 2,
                    )
                }
            }

//            Divider(
//                modifier = Modifier.padding(top = 18.dp).fillMaxWidth(),
//                thickness = 1.dp,
//                color = Color(0xFFEDEDED)
//            )
        }
    }
}
