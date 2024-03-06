package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.R
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.details.DetailsViewModel
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.util.Resource
import com.techafresh.jetreader.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun DetailScreenX(
    navController: NavController,
    bookId : String,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
){
    val isSaved = remember{
        mutableStateOf(false)
    }

    val firebaseID = ""

    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
        value = detailsViewModel.getBookInfo(bookId)
    }.value

    Scaffold(
        topBar = {
            AppBar2(
                title = "Details",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                isSaved = isSaved.value,
                onBackArrowClicked = {navController.navigate(AppScreensX.HomeScreenX.name){
                    navController.popBackStack()
                } },
                onDeleteBook = {
//                    deleteBook(bookInfo.data)
//                    isSaved.value = false
//                    Log.d("TAG", "DetailScreenX: Deleted a book")
                },
                onSaveBook = {
                    saveBook(book = bookInfo).run {
                        navController.navigate(AppScreensX.HomeScreenX.name){
                            navController.popBackStack()
                        }
                    }
//                    isSaved.value = true
//                    Log.d("TAG", "DetailScreenX: Saved a Book")
                }
            )
        }
    ){
        Surface(modifier = Modifier.padding(it)) {
//            if (isSaved.value){
//                saveBook(bookInfo)
//            }else{
//                deleteBook(bookInfo)
//            }
            DetailContent(book = bookInfo, navController = navController)
        }
    }
}

//@Composable
fun saveBook(book : Resource<Item>){

    val bookData = book.data?.volumeInfo
    val googleBookId = book.data?.id
//    val isBookEmpty = remember {
//        mutableStateOf(false)
//    }
    CoroutineScope(IO).launch {
        val db = FirebaseFirestore.getInstance()

        val book = MBook(
            title = bookData?.title.toString(),
            authors = bookData?.authors.toString(),
            description = bookData?.description.toString(),
            categories = bookData?.categories.toString(),
            notes = "",
            rating = 0.0,
            photoUrl = bookData?.imageLinks?.thumbnail.toString(),
            publishedDate = bookData?.publishedDate.toString(),
            pageCount = bookData?.pageCount.toString(),
            googleBookId = googleBookId.toString(),
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
        )

        if (book.toString().isNotEmpty() && !book.userId.isNullOrEmpty()) {
            db.collection("books").add(book)
                .addOnSuccessListener { documentReference ->
                    val stringId = documentReference.id
                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                    db.collection("books").document(documentReference.id)
                        .update(hashMapOf("id" to stringId) as Map<String, Any>).addOnCompleteListener {
                            if (it.isSuccessful){
                                Log.d("TAG", "SaveBook: Success \n Book Title = ${bookData?.title}")
                            }
                        }.addOnFailureListener {
                            Log.d("TAG", "SaveBook: Failure ", it)
                        }
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "SaveBook: Error adding document", e)
                }
        }
        else {
        }
    }

}

fun deleteBook(id: String){
    CoroutineScope(IO).launch {
        FirebaseFirestore.getInstance().collection("books")
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                Log.d("TAG", "deleteBook: Delete Book Successfully with id = ${id}")
                else
                    Log.d("TAG", "deleteBook: We have a problem")
            }
            .addOnFailureListener {
                Log.d("TAG", "deleteBook: Delete Book Failed with id = ${id}")
            }
    }
}
@Composable
fun DeleteBook(){}

@Composable
fun DetailContent(
    modifier : Modifier = Modifier,
    book: Resource<Item>,
    navController: NavController
){

    val interFamily = FontFamily(
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_black, FontWeight.Black)
    )

    val scrollState = rememberScrollState()


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        val bookData = book.data?.volumeInfo
        val googleBookId = book.data?.id

        Surface(modifier = Modifier
            .height(295.dp)
            .width(217.dp)) {
            AsyncImage(
                modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                model = bookData?.imageLinks?.thumbnail.toString(),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 25.dp, top = 30.dp, end = 2.dp)
                .align(Alignment.Start),
            text = bookData?.title.toString(),
            color = Color(0xFF0D0842),
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
//            letterSpacing = TextUnit(value = 1f, type = TextUnitType(1))

        )

        Text(
            modifier = Modifier
                .padding(start = 25.dp, top = 10.dp, end = 2.dp)
                .align(Alignment.Start),
            text = bookData?.authors.toString(),
            color = Color(0xFF0D0842).copy(0.8f),
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        )

        book.data?.searchInfo?.textSnippet?.let {
            Text(
                modifier = Modifier
                    .padding(start = 25.dp, top = 6.dp, end = 2.dp)
                    .align(Alignment.Start),
                text = it,
                color = Color(0xFF0D0842).copy(0.52f),
                fontFamily = interFamily,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip
            )
        }

        Divider(color = Color(0xFFEDEDED), modifier = Modifier
            .padding(top = 20.dp)
            .align(Alignment.Start))

        Text(
            modifier = Modifier
                .padding(start = 25.dp, top = 20.dp)
                .align(Alignment.Start),
            text = "Overview",
            color = Color(0xFF0D0842),
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )

        val descriptionText = HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        Text(
            modifier = Modifier
                .padding(start = 25.dp, top = 10.dp, end = 2.dp)
                .align(Alignment.Start),
            text = descriptionText,
            color = Color(0xFF0D0842).copy(0.52f),
            fontFamily = interFamily,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar2(
    title: String,
    icon : ImageVector? = null,
    isSaved: Boolean = false,
    navController: NavController,
    onBackArrowClicked : () -> Unit,
    onSaveBook : () -> Unit,
    onDeleteBook : () -> Unit
){

    val interFamily = FontFamily(
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_black, FontWeight.Black)
    )

    CenterAlignedTopAppBar(
        title = { /*if (showProfile) */ Text(text = title, color = Color(0xFF34A853), fontFamily = interFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        actions = {
            if (isSaved)
                IconButton(onClick = {
                    onDeleteBook.invoke()
                }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_bookmark_24), contentDescription = null, tint = Color(0xFF34A853))
                }
            else
                IconButton(onClick = {
                    onSaveBook.invoke()
                }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_bookmark_border_24), contentDescription = null)
                }
        },
        navigationIcon = {
            IconButton(onClick = { onBackArrowClicked.invoke()}) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}