package com.ezcall.presentation.screen.keypad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.ezcall.presentation.navigation.MainDestinations
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeypadScreen(
    viewModel: KeypadViewModel = hiltViewModel(),
    onNavigateToSubScreen: (String, NavBackStackEntry) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val state = rememberScrollState()
    val scope = rememberCoroutineScope()

    val callback = { text: String ->
        viewModel.handleButtonClick(text)
        scope.launch {
            state.animateScrollTo(state.maxValue)
        }
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = viewModel.numberToCall,
            onValueChange = viewModel::onNewNumberToCall,
            trailingIcon = {
                IconButton(onClick = { viewModel.onNewNumberToCall(viewModel.numberToCall.dropLast(1)) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        )

        MyRow(
            listOf("7", "8", "9"),
            listOf(0.33f, 0.33f, 0.33f),
            callback
        )
        MyRow(
            listOf("4", "5", "6"),
            listOf(0.33f, 0.33f, 0.33f),
            callback
        )
        MyRow(
            listOf("1", "2", "3"),
            listOf(0.33f, 0.33f, 0.33f),
            callback
        )
        MyRow(
            listOf("0"),
            listOf(0.33f),
            callback
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
            onClick = {
                viewModel.call()
                onNavigateToSubScreen(MainDestinations.IN_CALL_SCREEN, backStackEntry)
            }) {
            Text(text = "تماس")
        }
    }


}

@Composable
fun MyRow(
    texts: List<String>,
    weights: List<Float>,
    callback: (text: String) -> Any,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (i in texts.indices) {
            MyButton(
                text = texts[i],
                modifier = Modifier.weight(weights[i]),
                callback = callback
            )
        }
    }
}

@Composable
fun MyButton(
    text: String,
    callback: (text: String) -> Any,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .padding(4.dp),
        onClick = {
            callback(text)
        },
        shape = RoundedCornerShape(100),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)

    ) {
        Text(text)
    }
}