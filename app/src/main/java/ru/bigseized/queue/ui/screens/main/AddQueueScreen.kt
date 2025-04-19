package ru.bigseized.queue.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.bigseized.queue.R
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.ui.screens.AlertDialog
import ru.bigseized.queue.ui.screens.Screen
import ru.bigseized.queue.ui.screens.ShowProgressBar
import ru.bigseized.queue.viewModels.AddingQueueScreenViewModel
import ru.bigseized.queue.viewModels.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQueueScreen(
    navController: NavController,
    viewModel: AddingQueueScreenViewModel = viewModel(),
    mainScreenViewModel: MainScreenViewModel,
) {

    var isShowingProgress by remember {
        mutableStateOf(false)
    }

    var isShowingAlertDialog by remember {
        mutableStateOf(false)
    }

    var isNameOfQueueIsEmpty by remember {
        mutableStateOf(false)
    }

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val newQueueName: String by viewModel.nameOfNewQueue.collectAsState()
    val addQueueName: String by viewModel.nameOfAddQueue.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.adding_queue))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.MainScreen.name)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back button"
                        )
                    }
                })
        },

        ) { innerPadding ->
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.padding(innerPadding)
            ) {
                Tab(selected = selectedTabIndex == 0, onClick = {
                    selectedTabIndex = 0
                }) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.add)
                    )
                }
                Tab(selected = selectedTabIndex == 1, onClick = {
                    selectedTabIndex = 1
                }) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.create)
                    )
                }
            }

            // First tab ADDING
            if (selectedTabIndex == 0) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(id = R.string.code_of_queue),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            value = addQueueName,
                            isError = isNameOfQueueIsEmpty,
                            onValueChange = {
                                viewModel.updateNameOfAddQueue(it)
                                isNameOfQueueIsEmpty = it.isEmpty()
                            },
                            label = {
                                Text(stringResource(id = R.string.enter_code))
                            },
                            supportingText = {
                                if (!isNameOfQueueIsEmpty) {
                                    Text("")
                                } else {
                                    Text(stringResource(id = R.string.field_should_be_not_empty))
                                }
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                isShowingProgress = true
                                viewModel.addQueue()
                                mainScreenViewModel.starting(true)
                            },
                            modifier = Modifier.fillMaxWidth(),

                            ) {
                            Text(text = stringResource(id = R.string.add), fontSize = 16.sp)
                        }
                    }
                }
            } else { // Second tab CREATING
                Box(
                    Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(id = R.string.name_of_queue),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            value = newQueueName,
                            isError = isNameOfQueueIsEmpty,
                            onValueChange = {
                                viewModel.updateNameOfNewQueue(it)
                                isNameOfQueueIsEmpty = it.isEmpty()
                            },
                            label = {
                                Text(stringResource(id = R.string.enter_name))
                            },
                            supportingText = {
                                if (!isNameOfQueueIsEmpty) {
                                    Text("")
                                } else {
                                    Text(stringResource(id = R.string.field_should_be_not_empty))
                                }
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                isShowingProgress = true
                                viewModel.createQueue()
                            },
                            modifier = Modifier.fillMaxWidth(),

                            ) {
                            Text(text = stringResource(id = R.string.add), fontSize = 16.sp)
                        }
                    }
                }
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

            LaunchedEffect(viewModel.resultOfCreateQueue) {
                viewModel.resultOfCreateQueue.collect { result ->
                    isShowingProgress = false
                    when (result) {
                        is ResultOfRequest.Success -> {
                            mainScreenViewModel.starting(true)
                            // Sharing to window of queue info about its id
                            navController.navigate(Screen.QueueScreen.name + "/${result.result.id}")
                        }

                        is ResultOfRequest.Error -> {
                            errorMessage = result.errorMessage
                            isShowingAlertDialog = true
                        }

                        else -> {}
                    }
                }
            }
        }

    }
}