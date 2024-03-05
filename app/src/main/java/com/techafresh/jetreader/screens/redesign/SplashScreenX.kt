package com.techafresh.jetreader.screens.redesign

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.techafresh.jetreader.navigation.AppScreens
import com.techafresh.jetreader.navigation.redesign.AppScreensX
import com.techafresh.jetreader.screens.ReaderLogo
import kotlinx.coroutines.delay

@Composable
fun SplashScreenX(navController: NavController){
    val scale = remember{ Animatable(0f) }


    //Animation effect
    LaunchedEffect(key1 = true){
        scale.animateTo( targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        delay(2000L)
        //if there's a fb user, take user to Home Screen
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) navController.navigate(
            AppScreensX.SignupScreenX.name){
            navController.popBackStack()
        }
        else navController.navigate(AppScreensX.HomeScreenX.name){navController.popBackStack()}
    }


    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value)
        ,
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            ReaderLogo()

            Text(text = "\"Read. change. Yourself \"",
                modifier = Modifier.scale(scale.value),
                style = MaterialTheme.typography.headlineSmall, color = Color.LightGray)

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}