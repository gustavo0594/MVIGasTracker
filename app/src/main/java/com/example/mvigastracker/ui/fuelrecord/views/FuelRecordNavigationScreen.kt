package com.example.mvigastracker.ui.fuelrecord.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvigastracker.ui.fuelrecord.viewmodels.FuelRecordViewModel

@Composable
fun AddRecordNavigationScreen(
    navController: NavController,
    recordId: Int,
    modifier: Modifier = Modifier,
    viewModel: FuelRecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState
    AddRecordScreen(
        modifier = modifier,
        uiState = uiState,
        recordId = recordId,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = {
            navController.navigateUp()
        }
    )
}