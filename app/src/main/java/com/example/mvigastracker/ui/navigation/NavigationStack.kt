package com.example.mvigastracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mvigastracker.ui.fuelrecord.views.AddRecordNavigationScreen
import com.example.mvigastracker.ui.home.views.HomeNavigationScreen
import com.example.mvigastracker.ui.navigation.Args.RECORD_ID
import com.example.mvigastracker.ui.records.views.RecordsNavigationScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeNavigationScreen(navController = navController)
        }
        composable(route = Screen.Records.route) {
            RecordsNavigationScreen(navController = navController)
        }
        dialog(
            route = Screen.AddRecord.route,
            arguments = listOf(navArgument(name = RECORD_ID) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            AddRecordNavigationScreen(
                navController = navController,
                recordId = backStackEntry.arguments?.getInt(RECORD_ID, 0) ?: 0
            )
        }
    }
}

object Args {
    const val RECORD_ID = "RECORD_ID"
}

fun NavController.navigateToRecordDetail(recordId: Int = 0) {
    navigate(route = "add_record_screen/$recordId")
}