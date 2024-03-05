package com.techafresh.jetreader.screens.redesign

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.techafresh.jetreader.R
import com.techafresh.jetreader.components.EmailInput
import com.techafresh.jetreader.components.InputField
import com.techafresh.jetreader.components.PasswordInput
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.login.LoginViewModel
import com.techafresh.jetreader.util.showToast
import kotlin.math.log

@Composable
//@Preview
fun LoginScreenX(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    Scaffold(

    ) {
        Surface(modifier = Modifier.padding(it)) {
            UserForm2(
                title = "Log in",
                subText = "Welcome back! Log in to resume your reading journey.",
                btnText = "Log in",
                prefix = "Don’t have an account?",
                suffix = "Sign up",
                googleBtnText = "Login with Google",
                viewModel = viewModel,
                signUpOrLogin = {navController.navigate(AppScreensX.SignupScreenX.name){
                    navController.popBackStack()
                } },
                navController = navController
            ){email , password ->
                Log.d("TAG", "LoginScreenX: Email = $email , password = $password")
                viewModel.logInWithEmailAndPassword(email, password){
                    navController.navigate(AppScreensX.HomeScreenX.name){
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
//@Preview(showBackground = true)
fun UserForm2(title: String , subText : String, btnText : String , prefix : String, suffix : String , googleBtnText : String, navController: NavController, viewModel: LoginViewModel , signUpOrLogin : () -> Unit, onDone : (String , String) -> Unit){

    val ctx = LocalContext.current
    val interFamily = FontFamily(Font(R.font.inter_semibold, FontWeight.SemiBold),Font(R.font.inter_light, FontWeight.Light), Font(R.font.inter_regular, FontWeight.Normal), Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic), Font(R.font.inter_medium, FontWeight.Medium), Font(R.font.inter_bold, FontWeight.Bold), Font(R.font.inter_black, FontWeight.Black))
    val email = rememberSaveable{ mutableStateOf("") }
    val password = rememberSaveable{ mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequester = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
//        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(top = 76.dp)
                .fillMaxWidth(),
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = interFamily,
            color = Color.Black,
            textAlign = TextAlign.Left
        )

        Text(
            modifier = Modifier
                .padding(top = 15.dp),
            text = subText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = interFamily,
            color = Color(0xFF838589),
            textAlign = TextAlign.Left
        )

        Text(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 6.dp)
                .fillMaxWidth(),
            text = "Email address",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = interFamily,
            color = Color(0xFF0C1A30),
            textAlign = TextAlign.Left
        )

        EmailInput(emailState = email, enabled = true, onAction = KeyboardActions {
//            passwordFocusRequester.requestFocus()
        })

        Text(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 6.dp)
                .fillMaxWidth(),
            text = "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = interFamily,
            color = Color(0xFF0C1A30),
            textAlign = TextAlign.Left
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            passwordState = password,
            labelId = "Password",
            enabled = true,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                if (!valid) return@KeyboardActions
//                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hide() /* (to use this we need to use @ExperimentalComposeUiApi */
            }
        )
        
        // Button
        Button(
            modifier = Modifier
                .padding(top = 15.dp)
                .height(50.dp)
                .fillMaxWidth(),
            enabled = valid,
            shape = RoundedCornerShape(10.dp),
            colors = buttonColors(containerColor = Color(0xFF34A853)),
            elevation = ButtonDefaults.buttonElevation(10.dp),
            onClick = {
                keyboardController?.hide()
                onDone(email.value.trim(), password.value.trim())
            }
        ) {
            Text(
                text = btnText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = interFamily,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        FFF(fontFamily = interFamily, prefix = prefix, suffix = suffix){
            signUpOrLogin.invoke()
        }
        DivRoww(fontFamily = interFamily)

        OutlinedButton(
            modifier = Modifier
                .padding(3.dp)
                .height(50.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(listOf(Color(0xFF81D479), Color(0xFF81D479)))),
            onClick = { showToast(ctx , "Coming Soon...") }
        ) {
            GoogleSignIn(fontFamily = interFamily , text = googleBtnText)
        }


    }
}

@Composable
fun FFF(modifier : Modifier = Modifier, fontFamily: FontFamily, prefix : String, suffix : String, onClick: () -> Unit){
    Row(
        modifier = Modifier.padding(bottom = 35.dp, top = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = prefix,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamily,
            color = Color(0xFF838589),
        )
        Text(suffix,
            modifier = Modifier
                .clickable {
                    onClick.invoke()
                }
                .padding(start = 5.dp),

            fontWeight = FontWeight.Normal,
            color = Color(0xFF34A853)
        )
    }
}

//@Preview
@Composable
fun DivRoww(modifier : Modifier = Modifier, fontFamily: FontFamily){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 15.dp), horizontalArrangement = Arrangement.Center) {
        Divider(modifier = Modifier
            .width(150.dp)
            .padding(top = 10.dp))
        Text(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            text = "or",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamily,
            color = Color(0xFF838589),
        )
        Divider(modifier = Modifier
            .width(150.dp)
            .padding(top = 10.dp))
    }
}

@Composable
fun GoogleSignIn(fontFamily: FontFamily , text : String
//                 onClick : () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        IconButton(onClick = { onClick.invoke() }) {
            Image(
                painterResource(id = R.drawable.google),
                modifier = Modifier.size(16.dp),
                contentDescription = null)
//        }
        Text(
            modifier = Modifier.padding(start =8.dp),
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily,
            color = Color(0xFF34A853),
        )
    }
}