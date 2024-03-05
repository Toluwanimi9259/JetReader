package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.R
import com.techafresh.jetreader.components.HorizontalScrollableComponent
import com.techafresh.jetreader.components.ListCard
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.home.TitleSection
import com.techafresh.jetreader.screens.search.BookRow
import com.techafresh.jetreader.screens.search.SearchViewModel

@Composable
fun HomeScreenX(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel(), searchViewModel: SearchViewModel = hiltViewModel()){
    Scaffold(
        topBar = { AppBarX(icon = Icons.Filled.AccountCircle, title = "Jet-Reader", navController = navController){
            navController.navigate(AppScreensX.ProfileScreenX.name){popUpTo(AppScreensX.HomeScreenX.name)

            }
        }
                 },
        bottomBar = {
            HomeNav(navController = navController)
        }
    ) {
        val scrollState = rememberScrollState()
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            var listOfBooks = emptyList<MBook>()
            val currentUserName = if (!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (!viewModel.data.value.data.isNullOrEmpty()) {
                //****Filter books by current, logged in user***
                listOfBooks = viewModel.data.value.data?.toList()!!.filter { book ->
                    book.userId == currentUser?.uid.toString()
                }
            }
            if (searchViewModel.list.isNullOrEmpty()){

            }else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(start = 25.dp)
                ) {
                    Title("Recommended For You", "Handpicked based on your reading preferences")
                    Ja(searchViewModel = searchViewModel, navController = navController)
                    Title("Latest Releases", "Newly released books spanning various genres.")
                    HoriScroll(books = listOfBooks) {
                        navController.navigate(AppScreensX.DetailsScreenX.name +  "/$it")
                    }
                }
            }

//            Content(books = searchViewModel.list)

        }
    }
}

@Composable
fun Ja(searchViewModel: SearchViewModel, navController: NavController){
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp),
    ) {
        items(items = searchViewModel.list
//            .filter {
//            it.volumeInfo.imageLinks.smallThumbnail != null
        /*}*/) {
            BookColumn(book = it, navController = navController){
                navController.navigate(AppScreensX.DetailsScreenX.name +  "/$it")
            }
        }
    }
}


@Composable
fun BookColumn(
    book: Item,
    navController: NavController,
    onCardPress: (String) -> Unit
){
    val interFamily = FontFamily(
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_black, FontWeight.Black)
    )

    Column(
        modifier = Modifier
            .width(222.dp)
            .height(400.dp)
            .clickable {
                onCardPress.invoke(book.id)
            }
            .padding(start = 0.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Log.d("TAG", "BookColumn: ${book.volumeInfo.imageLinks}")
        AsyncImage(
            modifier = Modifier
                .height(251.dp)
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(5.dp)),
            model = if (book.volumeInfo.imageLinks.toString().isEmpty()) "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80" else book.volumeInfo.imageLinks.smallThumbnail,
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(1f),
            text = if (book.volumeInfo.title == null) "Title" else book.volumeInfo.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = interFamily,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF0D0842),
            textAlign = TextAlign.Left
        )

        Text(
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(1f),
            text =if (book.volumeInfo.description == null) "Description"   else book.volumeInfo.description.toString(), softWrap = true,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis,
            fontFamily = interFamily,
            color = Color(0xFF0D0842).copy(0.5f),
            maxLines = 2,
        )
    }
}


//@Preview
@Composable
fun Content(
    books : List<MBook>
){
//    Surface(
//        modifier = Modifier.fillMaxSize(1f)
//    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(start = 25.dp)
        ) {
//            Title( "Recommended For You")

//            HoriScroll(books = books){}
//            Title( "Latest Releases")
            HoriScroll(books = books){}
        }
//    }
}

@Composable
fun Title(text : String, subText: String){
    val interFamily = FontFamily(
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_black, FontWeight.Black)
    )
    Surface() {
        Column {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(1f),
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = interFamily,
                color = Color.Black,
                textAlign = TextAlign.Left
            )

            Text(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth(1f),
                text = subText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                fontFamily = interFamily,
                color = Color.Black,
                textAlign = TextAlign.Left
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun LlCard(
    book : MBook,
    onCardPress: (String) -> Unit
){

    val interFamily = FontFamily(
        Font(R.font.inter_light, FontWeight.Light),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_black, FontWeight.Black)
    )


    Column(
        modifier = Modifier
            .width(222.dp)
            .height(400.dp)
            .clickable { onCardPress.invoke(book.googleBookId.toString()) }
            .padding(start = 0.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        AsyncImage(
            modifier = Modifier
                .height(251.dp)
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(5.dp)),
            model = book.photoUrl,
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(1f),
            text = book.title!!,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = interFamily,
            color = Color(0xFF0D0842),
            textAlign = TextAlign.Left
        )

        Text(
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(1f),
            text = book.description!!,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            fontFamily = interFamily,
            color = Color(0xFF0D0842).copy(0.5f),
            maxLines = 2,
        )
    }
}

@Composable
fun HoriScroll(
    books: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPress: (String) -> Unit
){
    val scrollState = rememberScrollState()
   Row(
       modifier = Modifier
           .fillMaxWidth()
           .height(400.dp)
           .horizontalScroll(scrollState)
   ) {
       if (viewModel.data.value.loading == true) {
           LinearProgressIndicator()
           Log.d("TAG", "HoriScroll: Loading...")
       } else {
           Log.d("TAG", "HoriScroll: Done loading...")
           if (books.isEmpty()) {
               Surface(modifier = Modifier.padding(23.dp)) {
                   Text(text = "No books found. Add a book.",
                       style = TextStyle(
                           color = Color(0xFF34A853),
                           fontWeight = FontWeight.Bold,
                           fontSize = 14.sp,
                       ))
               }
           } else {
               for (book in books) {
                   Log.d("PH", "HoriScroll: ${book.googleBookId}")
                   LlCard(book){
                    onCardPress.invoke(it)
                   }
               }
           }
       }
   }
}

@Composable
fun HomeNav(navController: NavController){
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        NavigationBarItem(
            selected = currentDestination?.route == AppScreensX.HomeScreenX.name,
            onClick = { navController.navigate(AppScreensX.HomeScreenX.name) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text(text = "Home") }
        )

        NavigationBarItem(
            selected = currentDestination?.route == AppScreensX.SearchScreenX.name,
            onClick = { navController.navigate(AppScreensX.SearchScreenX.name) },
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text(text = "Explore") }
        )

        NavigationBarItem(
            selected = currentDestination?.route == AppScreensX.CollectionsScreenX.name,
            onClick = { navController.navigate(AppScreensX.CollectionsScreenX.name) },
            icon = { Icon(painterResource(id = R.drawable.baseline_collections_24), contentDescription = null) },
            label = { Text(text = "Collections") }
        )
    }
}