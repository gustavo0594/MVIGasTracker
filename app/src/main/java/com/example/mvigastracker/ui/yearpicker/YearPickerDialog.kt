package com.example.mvigastracker.ui.yearpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun YearPickerDialog(
    onYearSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    initialYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    startYear: Int = 1900,
    endYear: Int = Calendar.getInstance().get(Calendar.YEAR)
) {
    var selectedYear by remember { mutableIntStateOf(initialYear) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
           // elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Select Year",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val years = (startYear..endYear).toList().reversed()
                    items(years.size) { index ->
                        val year = years[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedYear = year
                                    onYearSelected(year)
                                    onDismissRequest()
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = year.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            if (year == selectedYear){
                                Icon(
                                    painter = rememberVectorPainter(Icons.Filled.Check),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearPickerSample() {
    var selectedYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var showYearPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Year: $selectedYear")

        Spacer(modifier = Modifier.height(4.dp))

        Button(onClick = { showYearPicker = true }) {
            Text(text = "Pick Year")
        }

        if (showYearPicker) {
            YearPickerDialog(
                initialYear = selectedYear,
                onYearSelected = { year ->
                    selectedYear = year
                },
                onDismissRequest = {
                    showYearPicker = false
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewYearPicker() {
    YearPickerSample()
}