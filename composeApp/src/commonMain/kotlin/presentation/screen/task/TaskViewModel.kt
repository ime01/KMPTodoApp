package presentation.screen.task

import RequestState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.TaskAction
import domain.TodoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import presentation.screen.home.MutableTasks
import presentation.screen.home.Tasks

class TaskViewModel(private val mongoDB: MongoDB): ScreenModel {


    fun setAction(action : TaskAction){
        when(action){
            is TaskAction.Add ->{
                addTask(action.task)
            }
            is TaskAction.Update -> {
                updateTask(action.task)
            }
            else -> {}

        }
    }


    private fun addTask(task:TodoTask){
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.addTask(task)
        }
    }

    private fun updateTask(task:TodoTask){
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.updateTask(task)
        }
    }



}