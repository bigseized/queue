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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import ru.bigseized.queue.viewModels.SignUpScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpScreenViewModel = viewModel()
) {
    val context = LocalContext.current

    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPassword by viewModel.userPassword.collectAsState()
    val userPasswordAgain by viewModel.userPasswordAgain.collectAsState()

    var isEmailCorrect by remember {
        mutableStateOf(false)
    }
    var isNameCorrect by remember {
        mutableStateOf(false)
    }
    var isPasswordCorrect by remember {
        mutableStateOf(false)
    }
    var isPasswordsEqual by remember {
        mutableStateOf(true)
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
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.LightGray, Color.Cyan)
                )
            )
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
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userEmail,
                isError = !isEmailCorrect,
                onValueChange = {
                    viewModel.updateEmail(it)
                    isEmailCorrect = isEmailCorrect(it)
                },
                label = {
                    Text(stringResource(id = R.string.enter_email))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                isError = !isNameCorrect,
                onValueChange = {
                    viewModel.updateName(it)
                    isNameCorrect = isNameCorrect(it)
                },
                label = {
                    Text(stringResource(id = R.string.enter_name))
                },
                supportingText = {
                    if (!isNameCorrect) {
                        Text(text = stringResource(id = R.string.field_should_be_not_empty))
                    } else {
                        Text("")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userPassword,
                maxLines = 1,
                isError = !isPasswordCorrect,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    viewModel.updatePassword(it)
                    isPasswordCorrect = isPasswordCorrect(it)
                    isPasswordsEqual = it == userPasswordAgain
                },
                label = {
                    Text(stringResource(id = R.string.enter_password))
                },
                supportingText = {
                    if (!isPasswordCorrect) {
                        Text(text = stringResource(id = R.string.password_is_too_easy))
                    } else {
                        Text("")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userPasswordAgain,
                maxLines = 1,
                isError = !isPasswordsEqual,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    viewModel.updatePasswordAgain(it)
                    isPasswordsEqual = userPassword == it
                },
                label = {
                    Text(stringResource(id = R.string.enter_password_again))
                },
                supportingText = {
                    if (!isPasswordsEqual) {
                        Text(text = stringResource(id = R.string.passwords_are_not_equal))
                    } else {
                        Text("")
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            FilledTonalButton(
                onClick = {
                    if (isPasswordsEqual && isEmailCorrect && isPasswordCorrect && isNameCorrect) {
                        isShowingProgress = true
                        viewModel.signUpUser()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.not_all_fields_filled),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.sign_up),
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
            Text(text = stringResource(id = R.string.already_have_account))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.sign_in),
                Modifier.clickable {
                    navController.navigate(Screen.SignInScreen.name)
                }
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
        LaunchedEffect(viewModel.result) {
            viewModel.result.collect { result ->
                isShowingProgress = false
                when (result) {
                    is ResultOfRequest.Success -> {
                        navController.navigate(Navigation.MAIN_ROUTE) {
                            popUpTo(Navigation.AUTH_ROUTE)
                        }
                    }
                    is ResultOfRequest.Error -> {
                        isShowingAlertDialog = true
                        errorMessage = result.errorMessage
                    }
                    else -> {}
                }
            }
        }
    }
}

fun isEmailCorrect(email: String): Boolean {
    val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    return emailRegex.matches(email)
}

fun isNameCorrect(name: String): Boolean {
    return name.isNotEmpty()
}

fun isPasswordCorrect(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")
    return passwordRegex.matches(password)
}