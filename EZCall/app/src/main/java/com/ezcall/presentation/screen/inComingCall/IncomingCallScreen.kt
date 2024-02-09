package com.ezcall.presentation.screen.inComingCall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry

@Composable
fun IncomingCallScreen(
    onNavigateToSubScreen: (String, NavBackStackEntry) -> Unit,
    backStackEntry: NavBackStackEntry,
) {

    Column(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Incoming call",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .background(shape = RoundedCornerShape(100) , color = Color.Green),
                onClick = { /*TODO*/ }) {

            }
            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .background(shape = RoundedCornerShape(100) , color = Color.Red),
                onClick = { /*TODO*/ }) {

            }


        }

    }

}