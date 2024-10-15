package com.example.mvigastracker.ui.home.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvigastracker.ui.base.SIDE_EFFECTS_KEY
import com.example.mvigastracker.ui.uicore.emptyscreen.EmptyScreen
import com.example.mvigastracker.ui.home.contracts.HomeContract
import com.example.mvigastracker.ui.home.entities.HomeChart
import com.example.mvigastracker.ui.home.entities.HomeViewState
import com.example.mvigastracker.ui.home.entities.UIFuelUp
import com.example.mvigastracker.ui.uicore.error.ErrorScreen
import com.example.mvigastracker.ui.uicore.loading.LoadingScreen
import com.example.mvigastracker.ui.yearpicker.YearPickerDialog
import com.konradgroup.charts.barchart.common.models.BarAnimationConfig
import com.konradgroup.charts.barchart.common.models.CategoryAxisConfig
import com.konradgroup.charts.barchart.common.models.bars.BarChartInputItem
import com.konradgroup.charts.barchart.vertical.views.VerticalBarChart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeContract.State,
    modifier: Modifier = Modifier,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: HomeContract.Effect.Navigation) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.Navigation.ToRecordList -> onNavigationRequested(effect)
                is HomeContract.Effect.Navigation.ToAddNewRecord -> onNavigationRequested(effect)
            }
        }?.collect()
    }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehavior,
                onMenuIconClicked = { onEventSent(HomeContract.Event.OnListMenuClicked) },
                yearSelected = homeUiState.yearSelected,
                onYearSelected = { onEventSent(HomeContract.Event.OnYearSelected(it)) },
            )
        },
        floatingActionButton = {
            if (homeUiState.homeViewState != null) {
                FloatingActionButton(
                    onClick = { onEventSent(HomeContract.Event.OnAddNewRecord) }
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
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when {
                homeUiState.isLoading -> LoadingScreen(Modifier.fillMaxSize())
                homeUiState.isError -> ErrorScreen(
                    onRetry = {
                        onEventSent(HomeContract.Event.Retry(yearSelected = homeUiState.yearSelected))
                    }
                )

                homeUiState.homeViewState != null -> Home(
                    homeViewState = homeUiState.homeViewState,
                )

                else -> EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    onAddNewRecord = { onEventSent(HomeContract.Event.OnAddNewRecord) }
                )
            }
        }
    }

}

@Composable
private fun Home(
    homeViewState: HomeViewState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RecordValueCard(
                modifier = Modifier.weight(1f),
                title = "Total Distance",
                painter = rememberVectorPainter(Icons.Default.CheckCircle),
                value = homeViewState.yearlyReport.totalDistance,
                subValue = homeViewState.yearlyReport.differenceFromLastEntry
            )
            RecordValueCard(
                modifier = Modifier.weight(1f),
                title = "Total Payment",
                painter = rememberVectorPainter(Icons.Default.CheckCircle),
                value = homeViewState.yearlyReport.totalAmount,
                subValue = homeViewState.yearlyReport.paymentPerKm
            )
        }
        homeViewState.lastFuelUp?.let { LatestStateCard(it) }
        ChartsSection(
            modifier = Modifier,
            paymentChartData = homeViewState.paymentChartData,
            distanceChartData = homeViewState.distanceChartData,
        )
        RecentEntries(fuelUps = homeViewState.recentFuelUps)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    yearSelected: Int? = null,
    onYearSelected: (year: Int) -> Unit,
    onMenuIconClicked: () -> Unit
) {
    val isCollapsed by remember {
        derivedStateOf { scrollBehavior.state.collapsedFraction > 0.5f }
    }

    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Column {
                Text("Fuel Dashboard")
                if (!isCollapsed) {
                    YearComponent(
                        selectedYear = yearSelected,
                        showAsIcon = false,
                        onYearSelected = onYearSelected,
                    )
                }
            }
        },
        actions = {
            if (isCollapsed) {
                YearComponent(
                    selectedYear = yearSelected,
                    showAsIcon = true,
                    onYearSelected = onYearSelected,
                )
            }
            IconButton(onClick = onMenuIconClicked) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun YearComponent(
    selectedYear: Int? = null,
    showAsIcon: Boolean,
    onYearSelected: (year: Int) -> Unit,
) {
    selectedYear?.let {
        val shouldOpenDatePicker = remember { mutableStateOf(false) }
        if (showAsIcon) {
            Icon(
                modifier = Modifier.clickable { shouldOpenDatePicker.value = true },
                painter = rememberVectorPainter(Icons.Filled.DateRange),
                contentDescription = null,
            )
        } else {
            AssistChip(
                onClick = { shouldOpenDatePicker.value = true },
                leadingIcon = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Filled.DateRange),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(text = "Selected Year: $selectedYear")
                }
            )
        }

        if (shouldOpenDatePicker.value) {
            YearPickerDialog(
                initialYear = selectedYear,
                onYearSelected = {
                    onYearSelected(it)
                    shouldOpenDatePicker.value = false
                },
                onDismissRequest = {
                    shouldOpenDatePicker.value = false
                }
            )
        }
    }
}

@Composable
private fun BarChartView(
    chartData: HomeChart,
    modifier: Modifier = Modifier,
) {
    VerticalBarChart(
        inputItems = chartData.dataSet.map {
            BarChartInputItem.SimpleBar(
                value = it.value,
                label = stringResource(it.label),
                color = MaterialTheme.colorScheme.primary
            )
        },
        scale = chartData.scale,
        categoryAxisConfig = CategoryAxisConfig(
            barThickness = 48.dp
        ),
        barAnimationConfig = BarAnimationConfig(animationEnabled = false),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}


@Composable
private fun RecordValueCard(
    title: String,
    painter: VectorPainter,
    value: String,
    subValue: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
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
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier.padding(top = 48.dp),
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = subValue,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun LatestStateCard(
    latestFuelUp: UIFuelUp,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Latest Fuel-Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date",
                    fontSize = 14.sp
                )
                Text(
                    text = latestFuelUp.date,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Odometer",
                    fontSize = 14.sp
                )
                Text(
                    text = latestFuelUp.kilometers,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Amount",
                    fontSize = 14.sp
                )
                Text(
                    text = latestFuelUp.totalAmount,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TabComponent(
    itemSelected: Int,
    values: List<String>,
    onItemSelected: (item: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(color = Color(0xffc3c3c3), shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        values.forEachIndexed { index, item ->
            TabItem(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                text = item,
                isSelected = itemSelected == index
            ) {
                onItemSelected(index)
            }
        }
    }
}

@Composable
private fun TabItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onItemSelected: () -> Unit
) {

    val textStyleSelected = TextStyle(
        fontSize = 14.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
    val textStyleUnselected = TextStyle(
        fontSize = 12.sp,
    )
    Box(
        modifier = if (isSelected) {
            modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onItemSelected() }
        } else {
            modifier.clickable { onItemSelected() }
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (isSelected) {
                textStyleSelected
            } else {
                textStyleUnselected
            }
        )
    }
}

@Composable
private fun ChartsSection(
    modifier: Modifier = Modifier,
    paymentChartData: HomeChart,
    distanceChartData: HomeChart,
) {
    val itemSelected = remember { mutableIntStateOf(0) }
    TabComponent(
        itemSelected = itemSelected.intValue,
        values = listOf("Payments", "Kilometers"),
        onItemSelected = { itemSelected.intValue = it }
    )
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val (text, chartData) = if (itemSelected.intValue == 0) {
                "Payments Over Time" to paymentChartData
            } else {
                "Kilometers Over Time" to distanceChartData
            }
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            BarChartView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                chartData = chartData,
            )
        }
    }
}

@Composable
private fun RecentEntries(
    fuelUps: List<UIFuelUp>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp, start = 8.dp, end = 8.dp),
                text = "Recent Entries",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            fuelUps.forEachIndexed { index, item ->
                EntryRow(
                    date = item.date,
                    distance = item.kilometers,
                    totalAmount = item.totalAmount
                )
                if (index < (fuelUps.size - 1)) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun EntryRow(
    date: String,
    distance: String,
    totalAmount: String,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                painter = rememberVectorPainter(Icons.Default.DateRange),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = date,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = distance,
                    fontSize = 14.sp,
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = totalAmount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }

}


@Preview
@Composable
private fun HomePreview() {
//    val homeViewState = HomeViewState(
//        totalKilometersByYear = "12300",
//        totalKilometers = "345500",
//        totalPaymentByYear = "520000",
//        totalPayment = "10300045",
//        paymentChartData = HomeChart(emptyList()),
//        distanceChartData = HomeChart(emptyList()),
//        yearSelected = 2024
//    )
//    RecentEntries()
//    TabComponent(
//        initialValue = 0,
//        values = listOf("Payments", "Kilometers"),
//        onItemSelected = {}
//    )
}
