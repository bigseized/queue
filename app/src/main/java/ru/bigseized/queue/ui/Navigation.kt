package ru.bigseized.queue.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.bigseized.queue.ui.screens.Screen
import ru.bigseized.queue.ui.screens.SplashScreen
import ru.bigseized.queue.ui.screens.logIn.SignInScreen
import ru.bigseized.queue.ui.screens.logIn.SignUpScreen
import ru.bigseized.queue.ui.screens.main.AddQueueScreen
import ru.bigseized.queue.ui.screens.main.HomeScreen
import ru.bigseized.queue.ui.screens.main.ProfileScreen
import ru.bigseized.queue.ui.screens.main.QueueScreen
import ru.bigseized.queue.ui.screens.main.SettingsScreen
import ru.bigseized.queue.viewModels.AddingQueueScreenViewModel
import ru.bigseized.queue.viewModels.MainScreenViewModel
import ru.bigseized.queue.viewModels.ProfileScreenViewModel
import ru.bigseized.queue.viewModels.QueueScreenViewModel
import ru.bigseized.queue.viewModels.SettingsScreenViewModel
import ru.bigseized.queue.viewModels.SignInScreenViewModel
import ru.bigseized.queue.viewModels.SignUpScreenViewModel
import ru.bigseized.queue.viewModels.SplashScreenViewModel

object Navigation {

    const val AUTH_ROUTE = "authRoute"
    const val MAIN_ROUTE = "mainRoute"
    const val SPLASH_ROUTE = "splashRoute"

    @Composable
    fun Navigation(
        mainScreenViewModel: MainScreenViewModel = hiltViewModel()
    ) {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SPLASH_ROUTE,
            enterTransition = { slideInHorizontally(initialOffsetX = { 300 }) + fadeIn() },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut() },
        ) {
            navigation(
                startDestination = Screen.SplashScreen.name,
                route = SPLASH_ROUTE
            ) {
                composable(Screen.SplashScreen.name) {
                    val viewModel = hiltViewModel<SplashScreenViewModel>()
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel,
                        mainScreenViewModel = mainScreenViewModel
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
                    HomeScreen(navController, mainScreenViewModel)
                }
                composable(Screen.ProfileScreen.name) {
                    val viewModel = hiltViewModel<ProfileScreenViewModel>()
                    ProfileScreen(navController, viewModel)
                }
                composable(Screen.SettingsScreen.name) {
                    val viewModel = hiltViewModel<SettingsScreenViewModel>()
                    SettingsScreen(navController, viewModel)
                }
                composable(Screen.AddQueueScreen.name) {
                    val viewModel = hiltViewModel<AddingQueueScreenViewModel>()
                    AddQueueScreen(navController, viewModel, mainScreenViewModel)
                }
                composable(
                    route = Screen.QueueScreen.name + "/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val id = it.arguments?.getString("id")
                    val viewModel = hiltViewModel<QueueScreenViewModel>()
                    QueueScreen(navController, viewModel, mainScreenViewModel, id ?: "")
                }
            }
        }
    }
}

