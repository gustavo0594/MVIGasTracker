package com.example.mvigastracker.core

import androidx.annotation.StringRes
import com.example.mvigastracker.R
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.dateFormat(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    // Use the user's default locale for output formatting
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}

fun Int.currencyFormat(): String {
    // Create a Costa Rica Locale
    val costaRicaLocale = Locale("es", "CR")

    // Get the currency instance for Costa Rica
    val currencyFormat = NumberFormat.getCurrencyInstance(costaRicaLocale).apply {
        maximumFractionDigits = 0
    }

    // Format the integer as a currency string (assuming it's in colones)
    return currencyFormat.format(this)
}

fun Int.distanceFormat():String =
    "$this km"

@StringRes
fun String.toMonthStringRes(): Int {
    val month = this.split("-")[1]
    return when (month) {
        "01" -> R.string.month_january
        "02" -> R.string.month_february
        "03" -> R.string.month_march
        "04" -> R.string.month_april
        "05" -> R.string.month_may
        "06" -> R.string.month_june
        "07" -> R.string.month_july
        "08" -> R.string.month_august
        "09" -> R.string.month_september
        "10" -> R.string.month_october
        "11" -> R.string.month_november
        else -> R.string.month_december
    }
}