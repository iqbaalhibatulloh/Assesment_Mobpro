package org.d3if0110.miniproject.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if0110.miniproject.ui.screen.AboutScreen
import org.d3if0110.miniproject.ui.screen.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), isDark: Boolean) {
    NavHost(
        navController = navController,
        startDestination =  Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController, isDark)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
    }
}