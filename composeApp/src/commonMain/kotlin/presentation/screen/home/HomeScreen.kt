package presentation.screen.home

import RequestState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.TaskAction
import domain.TodoTask
import presentation.screen.components.ErrorScreen
import presentation.screen.components.LoadingScreen
import presentation.screen.components.TaskView
import presentation.screen.task.TaskScreen

@OptIn(ExperimentalMaterial3Api::class)
class HomeScreen : Screen{


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeViewModel>()
        val activeTasks by viewModel.activeTasks
        val completedTasks by viewModel.completedTasks


        Scaffold(
            containerColor = Color(0xff127EEA),
            modifier = Modifier,
            topBar = {

                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color(0xff127EEA)),
                    title = { Text(text = "Home") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {navigator.push(TaskScreen())},
                    shape = RoundedCornerShape(size = 12.dp)
                ){
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
                }
            }
        ){padding ->

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding())
            ){
                DisplayTask(
                    modifier = Modifier.weight(1f),

                    tasks = activeTasks,

                    onSelect = {selectedTask ->
                        navigator.push(TaskScreen(selectedTask))
                    },

                    onFavorite = { task, isFavorite ->
                        viewModel.setAction(
                            action = TaskAction.SetFavorite(task, isFavorite)
                        )
                    },

                    onComplete = { task, completed ->
                        viewModel.setAction(
                            action = TaskAction.SetCompleted(task, completed)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                DisplayTask(
                    modifier = Modifier.weight(1f),
                    tasks = completedTasks,
                    showActive = false,
                    onDelete = {selectedTask ->
                        viewModel.setAction(
                            action = TaskAction.Delete(selectedTask)
                        )
                    },
                    onComplete = {selectedTask, completed ->
                        viewModel.setAction(
                            action = TaskAction.SetCompleted(selectedTask, completed)
                        )
                    }
                )
            }
        }

    }

}

@Composable
fun DisplayTask(
    modifier: Modifier = Modifier,
    tasks: RequestState<List<TodoTask>>,
    showActive:Boolean = true,
    onSelect: ((TodoTask) -> Unit)? = null,
    onFavorite: ((TodoTask, Boolean) -> Unit)? = null,
    onComplete: ((TodoTask, Boolean) -> Unit)? = null,
    onDelete: ((TodoTask) -> Unit)? = null,
){
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete:TodoTask? by remember { mutableStateOf(null) }

    if(showDialog){

        AlertDialog(
            title = {
                Text(
                    text = "Delete",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove this task: ${taskToDelete?.title}?",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },

            confirmButton = {
                Button(onClick = {
                    onDelete?.invoke(taskToDelete!!)
                    showDialog = false
                    taskToDelete = null
                }){
                    Text(text = "Yes")

                }
            },

            dismissButton = {
                TextButton(onClick = {
                    taskToDelete = null
                    showDialog = false
                }){
                    Text(text = "Cancel")
                }
            },

            onDismissRequest = {
                taskToDelete = null
                showDialog = false
            }
        )
    }


    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = if (showActive) "Active Tasks" else "Completed Tasks",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))

        tasks.DisplayResult(
            onLoading = { LoadingScreen() },

            onError = { ErrorScreen(message = it) },

            onSuccess = { listOfTasks ->

                if (listOfTasks.isNotEmpty()) {

                    LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {

                        items(
                            items = listOfTasks,
                            key = { task -> task._id.toHexString() }
                        ) { task ->
                            TaskView(
                                task = task,

                                showActive = showActive,

                                onSelect = { onSelect?.invoke(it) },

                                onComplete = { selectedTask, completed ->

                                    onComplete?.invoke(selectedTask, completed)
                                },
                                onFavorite = { selectedTask, favorite ->

                                    onFavorite?.invoke(selectedTask, favorite)
                                },
                                onDelete = { selectedTask ->
                                    taskToDelete = selectedTask
                                    showDialog = true
                                }

                            )
                        }

                    }

                } else {
                    ErrorScreen()
                }

            }
        )
    }

}
