package com.techafresh.jetreader.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.components.PetCardListItem
import com.techafresh.jetreader.components.RatingBar
import com.techafresh.jetreader.components.RoundedButton
import com.techafresh.jetreader.components.SimpleForm
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.screens.home.AppBarX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.util.Wrapper
import com.techafresh.jetreader.util.formatDate
import com.techafresh.jetreader.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun UpdateScreen(
    navController: NavController,
    bookItemId : String,
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            AppBarX(
                title = "Update",
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

        viewModel.getAllBooksFromDatabase()

        val df = rememberSaveable {
            mutableStateOf(false)
        }

//        val hfhfhf : Wrapper<List<MBook>, Boolean, Exception> = viewModel.getAllBooksFromDatabase()

        Surface(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(it))
        {
            Column(modifier = Modifier.padding(top = 3.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
//                Log.d("TAGINFO", "BookUpdateScreen: ${viewModel.data.value.data.toString()}")

                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                }else if (bookInfo.loading == false){
                    Surface(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                        shape = CircleShape,
                        shadowElevation = 4.dp) {

                        CoroutineScope(IO).launch {
                            viewModel.getAllBooksFromDatabase()
                            delay(10000)
                        }
                        if (viewModel.data.value.toString().isNotEmpty()) ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId, viewModel)
                        else Log.d("TAG", "UpdateScreen: Wahala dey oo")
                    }

                    //Add Textfield

                    if (viewModel.data.value.toString().isNotEmpty()) 
                    ShowSimpleForm(book = viewModel.data.value.data?.first { mBook ->
                        mBook.googleBookId == bookItemId
                    }!!, navController = navController)
                    
                    else Log.d("TAG", "UpdateScreen: Show Simple Form Sha")

                }

            }

        }
    }

}


@ExperimentalComposeUiApi
@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {
    val context = LocalContext.current
    val isStartedReading = remember{
        mutableStateOf(false)
    }
    val isFinishedReading = remember{
        mutableStateOf(false)
    }
    val ratingVal = remember {
        mutableStateOf(0)
    }
    val notesText = remember {
        mutableStateOf("")
    }

    SimpleForm(
        hint = "What do you think about the book?",
        defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString()
        else "No thoughts available :("

    ){ note ->
        notesText.value = note
        Log.d("Note", "ShowSimpleForm: $note")
        //invoke the buttons save click event
    }

    // Two Text buttons
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


    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    book.rating?.toInt()?.let {
        RatingBar(rating = it){rating ->
            ratingVal.value = rating
            Log.d("Rating", "ShowSimpleForm: $rating")
        }
    }


    Spacer(modifier = Modifier.padding(bottom = 15.dp))

    Row() {

        //Only update if the fields have new data!
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value

        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading

        val bookUpdate = changedNotes || changedRating
                || isStartedReading.value  || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value).toMap()

        RoundedButton(label = "Update"){
            if (bookUpdate) {
                FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                    .update(bookToUpdate).addOnCompleteListener {
                        showToast(context,"Book Updated Successfully!")
                        navController.navigate(AppScreens.HomeScreen.name)
                        Log.d("Update", "ShowSimpleForm: ${it.result.toString()}")
                    }.addOnFailureListener {
                        Log.w("TAG", "Error updating document", it)
                    }
            }
            //save or update
        }

        Spacer(modifier = Modifier.width(100.dp))

        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value){
            ShowAlertDialog(title = "Are you sure you want to delete this book? \n" +
                    "This action is not reversible", openDialog = openDialog) {
                CoroutineScope(IO).launch {
                    Log.d("TAG", "ShowSimpleForm: About To Delete Book id = ${book.id}")
                    FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                        .delete()
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                openDialog.value = false
                                /*
                                  Don't popBackStack() if we want the immediate recomposition
                                  of the MainScreen UI, instead navigate to the mainScreen!
                                 */
                                navController.navigate(AppScreens.HomeScreen.name)
//                            navController.popBackStack()
                            }
                        }.addOnFailureListener{
                            Log.d("ERRor", "ShowSimpleForm: $it")
                        }
                }
            }
        }

        RoundedButton(label = "Delete"){
            openDialog.value = true
            Log.d("TAG", "ShowSimpleForm: Delete Pressed")

        }
    }

}


@Composable
fun ShowAlertDialog(
    title: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            title = {
                Text(text = "Delete Book")
            },
            text = {
                Text(title)
            },
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = { onYesPressed.invoke() }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "No")
                }
            }
        )
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowBookUpdate(
    bookInfo: Wrapper<List<MBook>, Boolean, Exception>,
    bookItemId: String,
    viewModel: HomeScreenViewModel
) {
    Log.d("DEE", "In Show Book Update")
    Row {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Log.d("SBU", "ShowBookUpdate Before Crash: ${bookInfo.data!![0].id}")
                if (bookInfo.data.isNullOrEmpty()){
                    Log.d("TAG", "ShowBookUpdate: Book Info Data is null oo")
                }else{
                    CoroutineScope(IO).launch {
                        viewModel.getAllBooksFromDatabase()
                        delay(5000)
                        Log.d("DEE", "In Show Book Update after 5 secs")
                    }
                    Log.d("DEE", "In Show Book Before PetlistCard")
                    PetCardListItem(book = bookInfo.data!![0]
//                        .first
//                    { mBook ->
////                        Log.d("IN", "IDSS: ${mBook.googleBookId} ==> $bookItemId")
//                        Log.d("DEE", "in petlist card")
//                        mBook.googleBookId == bookItemId
//                    },
//                    onPressDetails = {}
                    )
                }
                

            }
        }
    }
}
