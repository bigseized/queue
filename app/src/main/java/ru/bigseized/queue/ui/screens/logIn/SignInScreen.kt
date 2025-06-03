package ru.bigseized.queue.ui.screens.logIn

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.bigseized.queue.R
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.ui.Navigation
import ru.bigseized.queue.ui.screens.AlertDialog
import ru.bigseized.queue.ui.screens.Screen
import ru.bigseized.queue.ui.screens.ShowProgressBar
import ru.bigseized.queue.viewModels.MainScreenViewModel
import ru.bigseized.queue.viewModels.SignInScreenViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInScreenViewModel = viewModel(),
    mainScreenViewModel: MainScreenViewModel,
) {
    val context = LocalContext.current

    val userEmail by viewModel.userName.collectAsState()
    val userPassword by viewModel.userPassword.collectAsState()

    var isEmailCorrect by remember {
        mutableStateOf(false)
    }
    var isPasswordCorrect by remember {
        mutableStateOf(false)
    }
    var isShowingProgress by remember {
        mutableStateOf(false)
    }
    var isShowingAlertDialog by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userEmail,
                isError = !isEmailCorrect,
                maxLines = 1,
                singleLine = true,
                onValueChange = {
                    viewModel.updateEmail(it)
                    isEmailCorrect = it.isNotEmpty()
                },
                label = {
                    Text(stringResource(id = R.string.enter_email))
                },
                supportingText = {
                    if (isEmailCorrect) {
                        Text("")
                    } else {
                        Text(stringResource(id = R.string.field_should_be_not_empty))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userPassword,
                maxLines = 1,
                singleLine = true,
                isError = !isPasswordCorrect,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    viewModel.updatePassword(it)
                    isPasswordCorrect = it.isNotEmpty()
                },
                label = {
                    Text(stringResource(id = R.string.enter_password))
                },
                supportingText = {
                    if (isPasswordCorrect) {
                        Text("")
                    } else {
                        Text(stringResource(id = R.string.field_should_be_not_empty))
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (isPasswordCorrect && isEmailCorrect) {
                        isShowingProgress = true
                        viewModel.signIn()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.not_all_fields_filled),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(
                    stringResource(id = R.string.sign_in),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.already_havent_account),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.sign_up),
                Modifier.clickable {
                    navController.navigate(Screen.SignUpScreen.name)
                },
                color = MaterialTheme.colorScheme.secondary
            )
        }

        if (isShowingProgress) {
            ShowProgressBar {
                isShowingProgress = false
            }
        }

        if (isShowingAlertDialog) {
            AlertDialog(
                onClick = {
                    isShowingAlertDialog = false
                },
                dialogTitle = stringResource(id = R.string.error),
                dialogText = errorMessage
            )
        }

        val resultOfSignIn = viewModel.result.collectAsState().value
        LaunchedEffect(resultOfSignIn) {
            isShowingProgress = false
            when (resultOfSignIn) {
                is ResultOfRequest.Success -> {
                    navController.navigate(Navigation.MAIN_ROUTE) {
                        popUpTo(Navigation.AUTH_ROUTE)
                        mainScreenViewModel.starting(context)
                    }
                }

                is ResultOfRequest.Error -> {
                    isShowingAlertDialog = true
                    errorMessage = resultOfSignIn.errorMessage
                }

                else -> {}
            }
        }
    }
}
