package com.example.mvigastracker.ui.uicore.emptyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvigastracker.R

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    onAddNewRecord: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(100.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                ),
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    painter = painterResource(R.drawable.ic_car),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                text = "No entries yet",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Add your first fuel-up to start tracking your vehicle's performance and expenses.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier.padding(top = 64.dp),
                onClick = onAddNewRecord
            ) {
                Text("Add your first Fuel-Up")
            }
        }
    }
}

@Composable
@Preview
private fun HomeEmptyScreenPreview() {
    EmptyScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        onAddNewRecord = {}
    )
}