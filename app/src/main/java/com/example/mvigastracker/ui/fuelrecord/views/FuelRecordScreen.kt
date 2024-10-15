package com.example.mvigastracker.ui.fuelrecord.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvigastracker.R
import com.example.mvigastracker.ui.base.SIDE_EFFECTS_KEY
import com.example.mvigastracker.ui.fuelrecord.contracts.FuelRecordContract
import com.example.mvigastracker.ui.fuelrecord.contracts.UIField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddRecordScreen(
    uiState: FuelRecordContract.State,
    modifier: Modifier = Modifier,
    recordId: Int,
    effectFlow: Flow<FuelRecordContract.Effect>?,
    onEventSent: (event: FuelRecordContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: FuelRecordContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onEventSent(FuelRecordContract.Event.FetchRecord(recordId))
        effectFlow?.onEach { effect ->
            when (effect) {
                is FuelRecordContract.Effect.Navigation.NavigateBack -> onNavigationRequested(effect)
            }
        }?.collect()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        FuelRecordForm(
            viewState = uiState,
            onValueUpdated = { onEventSent(it) },
            onSaveAction = { onEventSent(FuelRecordContract.Event.OnSaveAction) }
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(72.dp)
                .background(color = Color.White, shape = CircleShape)
        ) {
            Icon(
                modifier = Modifier.padding(12.dp),
                painter = painterResource(R.drawable.ic_gas_station),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun FuelRecordForm(
    viewState: FuelRecordContract.State,
    onValueUpdated: (action: FuelRecordContract.Event.OnValueUpdated) -> Unit,
    onSaveAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 40.dp)
    ) {
        DatePicker(
            selectedDate = viewState.date,
            onValueUpdated = {
                onValueUpdated(
                    FuelRecordContract.Event.OnValueUpdated(
                        value = it,
                        field = UIField.Date
                    )
                )
            })
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Kilometers",
            style = MaterialTheme.typography.labelLarge
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.kilometers,
            keyboardOptions = KeyboardOptions(
                showKeyboardOnFocus = true,
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            suffix = {
                Text(text = "km")
            },
            onValueChange = {
                onValueUpdated(
                    FuelRecordContract.Event.OnValueUpdated(
                        value = it,
                        field = UIField.Distance
                    )
                )
            },
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Total amount",
            style = MaterialTheme.typography.labelLarge
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.totalAmount,
            onValueChange = {
                onValueUpdated(
                    FuelRecordContract.Event.OnValueUpdated(
                        value = it,
                        field = UIField.Payment
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                showKeyboardOnFocus = true,
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            prefix = {
                Text(text = "$")
            },
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = onSaveAction
        ) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    selectedDate: String,
    onValueUpdated: (date: String) -> Unit,
) {
    val showDatePicker = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Date",
            style = MaterialTheme.typography.labelLarge
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedDate.toTime().toDate(),
            readOnly = true,
            onValueChange = {},
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { showDatePicker.value = true },
                    painter = rememberVectorPainter(Icons.Filled.DateRange),
                    contentDescription = null
                )
            }
        )
    }


    if (showDatePicker.value) {
        val datePickerState =
            rememberDatePickerState(
                initialDisplayedMonthMillis = selectedDate.toTime(),
                initialSelectedDateMillis = selectedDate.toTime(),
                yearRange = 2000..2024
            )
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker.value = false
                        onValueUpdated(datePickerState.selectedDateMillis.toDateFormatted())
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text(text = "Dismiss")
                }
            }) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

fun Long?.toDate(): String {
    val dateFormat = SimpleDateFormat("d MMM, y", Locale.getDefault())
    return try {
        dateFormat.format(this)
    } catch (t: Throwable) {
        t.printStackTrace()
        ""
    }
}

fun Long?.toDateFormatted(): String {
    val timeMillis = this?.plus(86400000)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        dateFormat.format(timeMillis)
    } catch (t: Throwable) {
        t.printStackTrace()
        ""
    }
}

fun String.toTime(): Long =
    try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)?.time
            ?: System.currentTimeMillis()
    } catch (t: Throwable) {
        t.printStackTrace()
        System.currentTimeMillis()
    }



@Composable
@Preview
private fun AddRecordScreenPreview() {
    // AddRecordScreen()
}