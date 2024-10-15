package com.example.mvigastracker.ui.uicore.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
        ) {
         CircularProgressIndicator(
             modifier = Modifier.size(60.dp)
         )
    }

}

@Composable
@Preview
private fun LoadingScreenPreview() {
    LoadingScreen()
}