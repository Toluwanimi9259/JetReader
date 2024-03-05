package com.techafresh.jetreader.screens.login


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techafresh.jetreader.components.EmailInput
import com.techafresh.jetreader.components.PasswordInput
import com.techafresh.jetreader.components.SubmitButton
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.screens.ReaderLogo
import kotlin.math.log

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val showLoginForm = rememberSaveable{ mutableStateOf(true) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ReaderLogo()
        if(showLoginForm.value) UserForm(false ,false) { email, password ->
            viewModel.logInWithEmailAndPassword(email, password){
                navController.navigate(AppScreens.HomeScreen.name)
            }
        }
        else UserForm(loading = false, isCreateAccount = true){ email, password ->
            viewModel.signUpWithEmailAndPassword(email , password){
                navController.navigate(AppScreens.HomeScreen.name)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            val text = if (showLoginForm.value) "Sign up" else "Login"
            Text("New User?")
            Text(text,
                modifier = Modifier
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }
                    .padding(start = 5.dp),

                fontWeight = FontWeight.Bold,
                color = Color.Red.copy(0.6f)
            )
        }

}
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit,
) {
    val email = rememberSaveable{ mutableStateOf("")}
    val password = rememberSaveable{ mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequester = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        if (isCreateAccount)
            Text(text = "Please enter a valid email and password that is at least 6 characters", modifier = Modifier.padding(4.dp))
        else {
            Text(text = "")
        }

        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            passwordFocusRequester.requestFocus()
        })

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
                //keyboardController?.hide() (to use this we need to use @ExperimentalComposeUiApi
            }
        )

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login" ,
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

    }

}
