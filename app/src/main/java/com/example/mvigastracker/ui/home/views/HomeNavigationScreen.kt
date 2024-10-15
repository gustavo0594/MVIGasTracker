package com.example.mvigastracker.ui.home.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvigastracker.ui.home.contracts.HomeContract
import com.example.mvigastracker.ui.home.viewmodels.HomeViewModel
import com.example.mvigastracker.ui.navigation.Screen
import com.example.mvigastracker.ui.navigation.navigateToRecordDetail

@Composable
fun HomeNavigationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.viewState
    HomeScreen(
        modifier = modifier,
        homeUiState = uiState,
        effectFlow = homeViewModel.effect,
        onEventSent = homeViewModel::handleEvents,
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is HomeContract.Effect.Navigation.ToRecordList ->
                    navController.navigate(Screen.Records.route)
                is HomeContract.Effect.Navigation.ToAddNewRecord ->
                    navController.navigateToRecordDetail()
            }
        }
    )
}