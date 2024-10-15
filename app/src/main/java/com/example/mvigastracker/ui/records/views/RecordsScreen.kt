package com.example.mvigastracker.ui.records.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvigastracker.R
import com.example.mvigastracker.ui.base.SIDE_EFFECTS_KEY
import com.example.mvigastracker.ui.uicore.emptyscreen.EmptyScreen
import com.example.mvigastracker.ui.home.contracts.HomeContract
import com.example.mvigastracker.ui.uicore.loading.LoadingScreen
import com.example.mvigastracker.ui.records.contracts.RecordListContract
import com.example.mvigastracker.ui.records.entities.UIFuelRecord
import com.example.mvigastracker.ui.uicore.error.ErrorScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    uiState: RecordListContract.State,
    modifier: Modifier = Modifier,
    effectFlow: Flow<RecordListContract.Effect>?,
    onEventSent: (event: RecordListContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RecordListContract.Effect.Navigation) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is RecordListContract.Effect.Navigation -> onNavigationRequested(effect)
            }
        }?.collect()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                scrollBehavior = scrollBehavior,
                onNavigationBack = { onEventSent(RecordListContract.Event.OnBackEvent) }
            )
        },
        floatingActionButton = {
            if (!uiState.isEmpty) {
                FloatingActionButton(
                    onClick = { onEventSent(RecordListContract.Event.OnAddNewRecord) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Add),
                        contentDescription = "Add new Record"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.isLoading -> LoadingScreen(Modifier.fillMaxSize())
                uiState.isEmpty -> EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    onAddNewRecord = { onEventSent(RecordListContract.Event.OnAddNewRecord) }
                )

                uiState.isError -> ErrorScreen(
                    onRetry = { onEventSent(RecordListContract.Event.Retry) }
                )

                else -> RecordsList(
                    recordList = uiState.records,
                    onItemClicked = { onEventSent(RecordListContract.Event.OnItemClicked(it)) }
                )
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationBack: () -> Unit
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Records",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Going Back"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun RecordsList(
    recordList: List<UIFuelRecord>,
    onItemClicked: (item: UIFuelRecord) -> Unit = {}
) {
    LazyColumn {
        items(items = recordList) { item ->
            RecordItem(
                uiFuelRecord = item,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
private fun RecordItem(
    uiFuelRecord: UIFuelRecord,
    onItemClicked: (item: UIFuelRecord) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(uiFuelRecord) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
                    .size(48.dp),
                painter = painterResource(R.drawable.ic_gas_station),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fuel up",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = uiFuelRecord.date,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = uiFuelRecord.distance,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = uiFuelRecord.totalCost,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

    }
}


@Composable
@Preview
private fun RecordItemPreview() {
    RecordItem(
        uiFuelRecord = UIFuelRecord(
            id = 1,
            date = "28 Sep 2024",
            distance = "34560 km",
            totalCost = "CRC40000"
        )
    )
}