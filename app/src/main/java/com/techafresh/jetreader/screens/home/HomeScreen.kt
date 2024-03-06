package com.techafresh.jetreader.screens.home

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.R
import com.techafresh.jetreader.components.HorizontalScrollableComponent
import com.techafresh.jetreader.components.ListCard
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX

@Composable
fun HomeScreen(navController: NavController , viewModel: HomeScreenViewModel = hiltViewModel()){
//    Text(text = "You Shouldn't be Here!!")
//    viewModel.getAllBooksFromDatabase()
    Home(navController = navController, viewModel)
}

@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel){
    Scaffold(
        topBar = { AppBarX(title = "Jet-Reader", navController = navController){
//            navController.navigate(AppScreensX.StatsScreenX.name)
        } },
        floatingActionButton = {
            Fab {
                navController.navigate(AppScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize(), color = Color.White) {
            Contents(navController = navController, viewModel)
        }
    }
}


@Composable
fun Contents(navController: NavController, viewModel: HomeScreenViewModel){
    val currentUserName = if (!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"

    val currentUser = FirebaseAuth.getInstance().currentUser

    var listOfBooks = emptyList<MBook>()

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        //****Filter books by current, logged in user***
        listOfBooks = viewModel.data.value.data?.toList()!!.filter { book ->
            book.userId == currentUser?.uid.toString()
        }

//        Log.d("TAG", "Contents: $listOfBooks")
    }
    Column(
        Modifier.padding(2.dp),
        Arrangement.Top
    ) {
        Row(
            Modifier.align(alignment = Alignment.Start)
        ){
            TitleSection(label = "Your reading \n " + " activity right now")
            Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.7f))
            Column {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            //Take user to stats screen
                            navController.navigate(AppScreens.StatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary)
                Text(currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                Divider()
            }
        }
//        ListCard(listOfBooks[0])

        ReadingRightNowArea(listOfBooks, navController, viewModel = viewModel)

        TitleSection(label = "Reading List")
        
        BookListArea(listOfBooks, viewModel = viewModel, navController = navController)
    }
}

@Composable
fun BookListArea(books: List<MBook>,navController: NavController, viewModel: HomeScreenViewModel) {
    val addedBooks = books.filter { book ->
        book.startedReading == null && book.finishedReading == null
    }
    HorizontalScrollableComponent(addedBooks) {
        navController.navigate(AppScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController, viewModel: HomeScreenViewModel) {
    //Filter by reading now books!
    val readingNowList = books.filter { book ->
        book.startedReading != null && book.finishedReading == null
    }
    HorizontalScrollableComponent(readingNowList) {
        navController.navigate(AppScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label : String){
    Surface(Modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(
                text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarX(
    title: String,
    icon : ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked : () -> Unit
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
        title = { /*if (showProfile) */Text(text = title, color = Color(0xFF34A853), fontFamily = interFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        actions = {
            if (showProfile)
                  IconButton(onClick = { FirebaseAuth.getInstance().signOut().run {
                      navController.navigate(AppScreensX.LoginScreenX.name){
                         navController.popBackStack()
                      }
                  }}) {
                      Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = null)
                  }
        },
        navigationIcon = {
                         IconButton(onClick = { onBackArrowClicked.invoke()}) {
                             if (icon != null) {
                                 Icon(imageVector = icon, contentDescription = null , modifier = Modifier.size(24.dp) , tint = Color(0xFF0C1A30))
                             }
                         }
        },
        colors = topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun Fab(onClick : () -> Unit){
    FloatingActionButton(
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF),
        onClick = { onClick() }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
    }
}