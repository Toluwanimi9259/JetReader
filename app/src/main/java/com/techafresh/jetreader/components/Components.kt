package com.techafresh.jetreader.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techafresh.jetreader.R
import com.techafresh.jetreader.model.MBook
import com.techafresh.jetreader.screens.home.HomeScreenViewModel


@Composable
fun BookRating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(56),
        shadowElevation = 4.dp,
        color = Color.White) {

        Column(modifier = Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.Star,
                contentDescription = "Star",
                modifier = Modifier.padding(3.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.labelMedium)
        }

    }

}
@Composable
fun RoundedButton(
    label: String = "lal", radius: Int = 29,
    onPress: () -> Unit = {},
) {
    Surface(modifier = Modifier
        //.size(200.dp)
        .clip(RoundedCornerShape(bottomEndPercent = radius, topStartPercent = radius)),
        color = Color(0xff92cbdf)

    ) {
        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = TextStyle(color = Color.White, fontSize = 15.sp),
                // modifier = Modifier.padding(20.dp)
            )
        }


    }


}


@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    val interFamily = FontFamily(
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_light, FontWeight.Light), Font(R.font.inter_regular, FontWeight.Normal), Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic), Font(R.font.inter_medium, FontWeight.Medium), Font(R.font.inter_bold, FontWeight.Bold), Font(R.font.inter_black, FontWeight.Black)
    )
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(
            text =  labelId,
            fontFamily = interFamily,
            fontSize = 14.sp,
            color = Color.Black.copy(0.5f)
        ) },
        singleLine = true,
        textStyle = TextStyle(fontFamily = interFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
//            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Lock, contentDescription = "")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF0C1A30),
            unfocusedTextColor = Color(0xFF0C1A30),
            focusedBorderColor = Color(0xFFEDEDED),
            unfocusedBorderColor = Color(0xFFEDEDED),
            focusedContainerColor = Color(0xFFD9D9D9).copy(0.5f),
            unfocusedContainerColor = Color(0xFFD9D9D9).copy(0.5f),
        )

    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    val description = if (visible) "" else "*******"
    //stringResource(id = if (visible) R.string.hide_password else R.string.show_password)
    val icon =
        painterResource(id = if (visible) R.drawable.baseline_remove_red_eye_24 else R.drawable.baseline_visibility_off_24)

    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(painter = icon, contentDescription = "")
        //Icon(painter = null, contentDescription = description)
    }
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField2(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction,
        leadingIcon = Icons.Filled.Email
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String> = mutableStateOf(""),
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    leadingIcon : ImageVector? = null
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text =  labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = "")
            }
        }
    )
}

@Composable
fun InputField2(
    modifier: Modifier = Modifier,
    valueState: MutableState<String> = mutableStateOf(""),
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    leadingIcon : ImageVector? = null
) {

    val interFamily = FontFamily(Font(R.font.inter_semibold, FontWeight.SemiBold),Font(R.font.inter_light, FontWeight.Light), Font(R.font.inter_regular, FontWeight.Normal), Font(R.font.inter_light, FontWeight.Normal, FontStyle.Italic), Font(R.font.inter_medium, FontWeight.Medium), Font(R.font.inter_bold, FontWeight.Bold), Font(R.font.inter_black, FontWeight.Black))
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = {
            Text(
                text =  labelId,
                fontFamily = interFamily,
                fontSize = 14.sp,
                color = Color.Black.copy(0.5f)
            )
                },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontFamily = interFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
//            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = "")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF0C1A30),
            unfocusedTextColor = Color(0xFF0C1A30),
            focusedBorderColor = Color(0xFFEDEDED),
            unfocusedBorderColor = Color(0xFFEDEDED),
            focusedContainerColor = Color(0xFFD9D9D9).copy(0.5f),
            unfocusedContainerColor = Color(0xFFD9D9D9).copy(0.5f),
        )
    )
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape,
        onClick = onClick
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}



@Composable
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    numberState: MutableState<String>,
    labelId: Int = 2345,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField2(
        modifier = modifier,
        valueState = numberState,
        labelId = labelId.toString(),
        enabled = enabled,
        keyboardType = KeyboardType.Phone,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun Header(textId: Int) {
    Text(
        text = stringResource(id = textId),
        modifier = Modifier.padding(vertical = 50.dp, horizontal = 15.dp),
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Preview(showBackground = true)
@Composable
fun GG(){
    val dd = remember{ mutableStateOf("") }
    OutlinedTextField(
        value = dd.value,
        onValueChange = {dd.value = it},
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Lock, contentDescription = "")
        }
    )
}

@Composable
fun HorizontalScrollableComponent(books: List<MBook>,
                                  viewModel: HomeScreenViewModel = hiltViewModel(),
                                  onCardPress: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .horizontalScroll(scrollState)) {

        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()
            Log.d("TAG", "HorizontalScrollableComponent: Loading...")
        } else {
            Log.d("TAG", "HorizontalScrollableComponent: Done loading...")
            if (books.isEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a book.",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ))
                }
            } else {
                for (book in books) {
                    Log.d("PH", "HorizontalScrollableComponent: ${book.googleBookId}")
                    ListCard(book){
                        onCardPress(book.googleBookId.toString())
                    }
                }
            }
        }

    }
}
