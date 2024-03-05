package com.techafresh.jetreader.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.components.RoundedButton
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.util.Resource

@Composable
fun DetailScreen(
    navController: NavController,
    bookId : String,
    detailsViewModel: DetailsViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            AppBarX(
                title = "Book Details",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
            ) {
                navController.navigate(AppScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(it)) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = detailsViewModel.getBookInfo(bookId)
                }.value

//                if (bookInfo.data == null){
//                    LinearProgressIndicator()
//                }else{
//                    Text(text = "Book id: ${bookInfo.data?.volumeInfo!!.title}")
//                }

                ShowBookDetails(bookInfo, navController = navController)
            }

        }
    }
}

@Composable
fun ShowBookDetails(book: Resource<Item>, navController: NavController) {
    val bookData = book.data?.volumeInfo
    val googleBookId = book.data?.id
    val isBookEmpty = remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier.padding(34.dp),
        //.size(90.dp),
        shape = CircleShape, elevation = cardElevation(4.dp)) {
        Image(painter = rememberAsyncImagePainter(bookData?.imageLinks?.thumbnail.toString()),
            contentDescription = "Book image",
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp))

    }
    Text(text = bookData?.title.toString(),
        style = MaterialTheme.typography.labelSmall,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19)

    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(text = "Categories: ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.labelMedium)
    Text(text = "Published: ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.labelMedium)
    Spacer(modifier = Modifier.height(5.dp))

    val localDims = LocalContext.current.resources.displayMetrics
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                val cleanText = HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                Text(text = cleanText,
                    modifier = Modifier.padding(4.dp),
                )
            }
        }
    }

    //Buttons
    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround) {

        RoundedButton(label = "Save" ){
            //save this book to firestore
            val db = FirebaseFirestore.getInstance()

            val book = MBook(
                title = bookData?.title.toString(),
                authors = bookData?.authors.toString(),
                description = bookData?.description.toString(),
                categories = bookData?.categories.toString(),
                notes = "",
                photoUrl = bookData?.imageLinks?.thumbnail.toString(),
                publishedDate = bookData?.publishedDate.toString(),
                pageCount = bookData?.pageCount.toString(),
                googleBookId = googleBookId.toString(),
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            )

            if (book.toString().isNotEmpty()) {
                db.collection("books").add(book)
                    .addOnSuccessListener { documentReference ->
                        val stringId = documentReference.id
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                        //TODO: update this saved book with the documentId
                        db.collection("books").document(documentReference.id)
                            .update(hashMapOf("id" to stringId) as Map<String, Any>).addOnCompleteListener {
                                if (it.isSuccessful){
                                    navController.popBackStack()
                                }
                            }.addOnFailureListener {
                                Log.w("TAG", "Error updating document", it)
                            }
                        //take them back...
                }
                    .addOnFailureListener { e ->
                         Log.w("TAG", "Error adding document", e)
                }
            } else {
                isBookEmpty.value = true
            }
        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel" ){
            //save this book to firestore
            navController.popBackStack()
        }

    }
    if (isBookEmpty.value) Text(text = "Cannot save empty books", color = Color.Red.copy(alpha = 0.5f)
    ) else Text(text = "")


}