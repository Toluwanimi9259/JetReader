package com.techafresh.jetreader.screens.redesign

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun RecommendationScreen(navController: NavController){

}

@Preview(showBackground = true)
@Composable
fun Content(){
    val checkedList = remember { mutableStateListOf<Int>() }
    val options = listOf("Favorites", "Trending", "Saved")
    val icons = listOf(
        Icons.Filled.Email,
        Icons.Filled.Person,
        Icons.Filled.Settings
    )
//    MultiChoiceSegmentedButtonRow {
//        options.forEachIndexed { index, label ->
//            SegmentedButton(
//                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
//                icon = {
//                    SegmentedButtonDefaults.Icon(active = index in checkedList) {
//                        Icon(
//                            imageVector = icons[index],
//                            contentDescription = null,
//                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
//                        )
//                    }
//                },
//                onCheckedChange = {
//                    if (index in checkedList) {
//                        checkedList.remove(index)
//                    } else {
//                        checkedList.add(index)
//                    }
//                },
//                checked = index in checkedList
//            ) {
//                Text(label)
//            }
//        }
//    }

}
