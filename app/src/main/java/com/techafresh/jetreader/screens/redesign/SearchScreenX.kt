package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.techafresh.jetreader.R
import com.techafresh.jetreader.model.Item
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.search.SearchViewModel

@Composable
fun SearchScreenX(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            AppBarX(
                title = "Explore",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
            ){
                navController.navigate(AppScreensX.HomeScreenX.name){
                    navController.popBackStack()
                }
            }
        }
    ) {
        val scrollState = rememberScrollState()
        Surface(modifier = Modifier
            .padding(it)
            .verticalScroll(scrollState)) {
            Column {
                ExploreList(searchViewModel = viewModel, navController = navController)
            }
        }
    }
}

@Composable
fun Ja2(searchViewModel: SearchViewModel,navController: NavController,  books : List<Item> , loader : Boolean = true){
//    if (searchViewModel.fictionIsLoading.value){
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    }else{
        Log.d("JA2", "Ja2: List = $books")
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        ) {
            items(items = books
                .filter {
                it.volumeInfo.imageLinks != null
            }
            ) {
                BookColumn(book = it, navController = navController){
                    navController.navigate(AppScreensX.DetailsScreenX.name +  "/$it")
                }
            }
//        }
    }

}


@Composable
fun ExploreList(searchViewModel: SearchViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp)
    ) {

        Title("Romance", "Explore the world of steamy romance")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.fictionList)

        Title("Mystery", "Explore the world of mystery")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.mysteryList)

        Title("Horror", "Explore the  world of horror")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.horrorList)

        Title("Self-Help", "Motivational and empowering guides to personal growth and well-being")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.selfHelpList)

        Title("Biography", "Informative accounts of real people’s lives")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.biographyList)

        Title("Thriller", "Tense, suspenseful stories that keep readers on the edge of their seats")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.thrillerList)

        Title("Adventure", "Exciting tales of risk-taking and heroism.")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.adventureList)

        Title("Fantasy", "Involves world-building and characters who are supernatural, mythological, magical, or a combination of these")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.fantasyList)

        Title("Cooking", "Learn how to cook")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.cookingList)

        Title("Dystopian", "Books Often set in a bleak future (near or distant) to explore cultural or social issues")
        Ja2(navController = navController , searchViewModel = searchViewModel, books = searchViewModel.dystopianList)
    }
}

@Composable
fun BookRow2(book: MBook, navController: NavController, onClick : () -> Unit){
    Column(
        modifier = Modifier
            .height(130.dp).clickable { onClick.invoke() }
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row()
        {

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
                    .height(97.dp)
                    .width(57.dp), shape = RoundedCornerShape(5.dp)
            ) {
                AsyncImage(
                    model = book.photoUrl,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.padding(start = 15.dp)
            ) {

                Text(
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding()
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
                        .padding(top = 2.dp)
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
                        .padding(top = 10.dp)
                        .fillMaxWidth(1f),
                    text = if (book.description.isNullOrEmpty()) "Description" else descriptionText,
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

        Divider(
            modifier = Modifier.padding(top = 18.dp).fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFEDEDED)
        )
    }
}


@Composable
fun BookRowX(
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
        elevation = CardDefaults.cardElevation(6.dp)){
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

@Composable
fun BookListX(
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
//                BookRow2(book = item, navController = navController)
            }
        }
    }
}