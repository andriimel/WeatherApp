package com.am.weatherapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
    fun SearchBar(isVisible: Boolean,
         searchText:String,
        onSearchTextChange: (String) -> Unit,
                  onSearchClick:() -> Unit

        ) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Column(modifier = Modifier.padding(top = 30.dp)) {

            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                maxLines = 1,
                placeholder = { Text("Enter city name") },
                trailingIcon = {
                    IconButton(onClick = { onSearchClick()}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier

                    .fillMaxWidth()

            )
        }
    }

}