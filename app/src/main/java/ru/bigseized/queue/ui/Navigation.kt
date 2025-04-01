package ru.bigseized.queue.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ru.bigseized.queue.ui.screens.HomeScreen
import ru.bigseized.queue.ui.screens.Screen
import ru.bigseized.queue.ui.screens.SignInScreen
import ru.bigseized.queue.ui.screens.SignUpScreen

object Navigation {

    const val AUTH_ROUTE = "authRoute"
    private const val MAIN_ROUTE = "mainRoute"

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AUTH_ROUTE
        ) {
            navigation(
                startDestination = Screen.SignUpScreen.name,
                route = AUTH_ROUTE
            ) {
                composable(Screen.SignInScreen.name) {
                    SignInScreen(navController)
                }
                composable(Screen.SignUpScreen.name) {
                    SignUpScreen(navController)
                }
            }
            navigation(
                startDestination = Screen.MainScreen.name,
                route = MAIN_ROUTE
            ) {
                composable(Screen.MainScreen.name) {
                    HomeScreen()
                }
            }
        }
    }
}

