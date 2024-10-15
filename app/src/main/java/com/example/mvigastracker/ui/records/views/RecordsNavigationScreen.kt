package com.example.mvigastracker.ui.records.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvigastracker.ui.navigation.navigateToRecordDetail
import com.example.mvigastracker.ui.records.contracts.RecordListContract
import com.example.mvigastracker.ui.records.viewmodels.RecordListViewModel

@Composable
fun RecordsNavigationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecordListViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState
    RecordsScreen(
        modifier = modifier,
        uiState = uiState,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                RecordListContract.Effect.Navigation.NavigateBack ->
                    navController.popBackStack()

                RecordListContract.Effect.Navigation.ToAddNewRecord ->
                    navController.navigateToRecordDetail()

                is RecordListContract.Effect.Navigation.ToEditRecord ->
                    navController.navigateToRecordDetail(navigationEffect.recordId)
            }
        }
    )
}