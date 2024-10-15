package com.example.mvigastracker.ui.navigation

import com.example.mvigastracker.ui.navigation.Args.RECORD_ID

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Records : Screen("records_screen")
    data object AddRecord : Screen("add_record_screen/{$RECORD_ID}")
}