package com.techafresh.jetreader.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.techafresh.jetreader.R
import com.techafresh.jetreader.model.MBook


//@Preview
@Composable
fun ListCard(
    book: MBook,
    onPressDetails: (String) -> Unit,
) {

    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    // Compute the screen width using the actual display width and the density of the display.
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    //Details and reading
    val isStartedReading = remember {
        mutableStateOf(false)
    }

    Card(
        shape = RoundedCornerShape(29.dp),
        colors = cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable {
                       onPressDetails(book.title!!)
            },

        ) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start,
            ){
            Row(horizontalArrangement = Arrangement.Center) {
//
                //  Log.d("CARD", "ListCard: Imageurl:${book.photoUrl.toString()}")
                Image(
                    painter = rememberAsyncImagePainter(
                        book.photoUrl
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))

                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //heart here
                    Icon(imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        modifier = Modifier.padding(bottom = 1.dp))

                    //card with star on top and 4.5 bottom
                    BookRating(score = 4.6)
                }
            }

            Text(
                text = book.title.toString(), modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.labelMedium)

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
//            isStartedReading.value = book.startedReading != null

//            Spacer(modifier = Modifier.padding(end = 12.dp))
//            Text(text = "Details",
//                modifier = Modifier.padding(12.dp),
//                style = MaterialTheme.typography.caption)

                RoundedButton(label = if (isStartedReading.value) "Reading" else "Not Started", radius = 70)
            }

        }




    }

}


//Rating Bar
@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableIntStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


@Composable
fun PetCardListItem(book: MBook,
//                    onPressDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                //onPetClick(pet)
            },
        elevation = cardElevation(8.dp)
    ) {

        Row(horizontalArrangement = Arrangement.Start) {
            Image(painter = rememberAsyncImagePainter(book.photoUrl.toString()),
                contentDescription = null,
                modifier = Modifier.height(100.dp).width(120.dp).padding(4.dp).clip(RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 20.dp))
            )
            Column {
                Row {
                    //AgeChip(pet = book)
                    // GenderIcon(pet = book)
                }
                // BehaviourChip(book)
                Text(book.title.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp).width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    book.authors.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 0.dp)
                )
                Text(
                    book.publishedDate.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp)
                )
            }
        }

    }

}
