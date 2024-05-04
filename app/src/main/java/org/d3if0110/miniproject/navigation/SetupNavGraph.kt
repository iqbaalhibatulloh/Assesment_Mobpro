package org.d3if0110.miniproject.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if0110.miniproject.ui.screen.MainScreenModel
import org.d3if0110.miniproject.ui.screen.AboutScreen
import org.d3if0110.miniproject.ui.screen.DetailScreen
import org.d3if0110.miniproject.ui.screen.KEY_ID_NOTE
import org.d3if0110.miniproject.ui.screen.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), mainScreenModel: MainScreenModel = MainScreenModel()) {
    NavHost(
        navController = navController,
        startDestination =  Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController, mainScreenModel)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_NOTE) { type = NavType.LongType }
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_NOTE)
            DetailScreen(navController, id)
        }
    }
}