package ru.bigseized.queue.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.bigseized.queue.R
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.domain.model.User
import ru.bigseized.queue.ui.Navigation
import ru.bigseized.queue.viewModels.MainScreenViewModel
import ru.bigseized.queue.viewModels.SplashScreenViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel,
    mainScreenViewModel: MainScreenViewModel,
) {
    val scale = remember {
        Animatable(0f)
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val job = launch(Dispatchers.Main) {
            scale.animateTo(
                targetValue = 1.7f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        launch(Dispatchers.IO) {
            viewModel.starting()
        }

        job.join()
        viewModel.result.collect { result ->
            navigationToNextScreen(result, navController)
        }
    }

    val result = viewModel.result.collectAsState().value
    LaunchedEffect(result) {
        when (result) {
            is ResultOfRequest.Success -> {
                mainScreenViewModel.starting(context)
            }

            else -> {}
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }

}

fun navigationToNextScreen(result: ResultOfRequest<User?>?, navController: NavController) {
    when (result) {
        is ResultOfRequest.Error -> {
            navController.navigate(Navigation.AUTH_ROUTE) {
                popUpTo(Navigation.SPLASH_ROUTE)
            }
        }

        is ResultOfRequest.Success -> {
            navController.navigate(Navigation.MAIN_ROUTE) {
                popUpTo(Navigation.SPLASH_ROUTE)
            }
        }

        else -> {}
    }
}