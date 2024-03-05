package com.techafresh.jetreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.navigation.AppNavigation
import com.techafresh.jetreader.navigation.redesign.AppNavigationX
import com.techafresh.jetreader.ui.theme.JetReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetReaderTheme {

//                val db = FirebaseFirestore.getInstance()
//                val user : MutableMap<String, Any> = HashMap()
//                user["firstname"] = "Curtis"
//                user["lastname"] = "Jones"
//
//                db.collection("users")
//                    .add(user)
//                    .addOnSuccessListener {
//                        Log.d("TAG", "onCreate: ${it.id}")
//                    }
//                    .addOnFailureListener{
//                        Log.d("TAG", "onCreate: ${it.message}")
//                    }


                ReaderAppp()


                // A surface container using the 'background' color from the theme

            }
        }
    }
}


@Composable
fun ReaderAppp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            AppNavigation()
            AppNavigationX()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetReaderTheme {
    }
}