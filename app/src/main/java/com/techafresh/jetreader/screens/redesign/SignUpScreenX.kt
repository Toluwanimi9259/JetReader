package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.login.LoginViewModel

@Composable
fun SignUpScreenX(navController: NavController,viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    Scaffold(

    ) {
        Surface(modifier = Modifier.padding(it).fillMaxSize(), color = Color.White) {
            UserForm2(
                title = "Create your account",
                subText = "Create an account and explore a tailored library of captivating stories.",
                btnText = "Create new account",
                prefix = "Already have an account",
                suffix = "Login",
                googleBtnText = "Sign up with Google",
                viewModel = viewModel,
                signUpOrLogin = {navController.navigate(AppScreensX.LoginScreenX.name){
                    navController.popBackStack()
                } },
                navController = navController
            ){email , password ->
                Log.d("TAG", "SignUpScreen: Email = $email , password = $password")
                viewModel.signUpWithEmailAndPassword(email , password){
                    navController.navigate(AppScreensX.HomeScreenX.name){
                       navController.popBackStack()
                    }
                }
            }

        }
    }
}