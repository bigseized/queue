package ru.bigseized.queue.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ru.bigseized.queue.ui.screens.HomeScreen
import ru.bigseized.queue.ui.screens.Screen
import ru.bigseized.queue.ui.screens.SignInScreen
import ru.bigseized.queue.ui.screens.SignUpScreen
import ru.bigseized.queue.ui.screens.SplashScreen
import ru.bigseized.queue.viewModels.SignInScreenViewModel
import ru.bigseized.queue.viewModels.SignUpScreenViewModel
import ru.bigseized.queue.viewModels.SplashScreenViewModel

object Navigation {

    const val AUTH_ROUTE = "authRoute"
    const val MAIN_ROUTE = "mainRoute"
    const val SPLASH_ROUTE = "splashRoute"

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SPLASH_ROUTE
        ) {
            navigation(
                startDestination = Screen.SplashScreen.name,
                route = SPLASH_ROUTE
            ) {
                composable(Screen.SplashScreen.name) {
                    val viewModel = hiltViewModel<SplashScreenViewModel>()
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
            navigation(
                startDestination = Screen.SignUpScreen.name,
                route = AUTH_ROUTE
            ) {
                composable(Screen.SignInScreen.name) {
                    val viewModel = hiltViewModel<SignInScreenViewModel>()
                    SignInScreen(navController, viewModel)
                }
                composable(Screen.SignUpScreen.name) {
                    val viewModel = hiltViewModel<SignUpScreenViewModel>()
                    SignUpScreen(navController, viewModel)
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

