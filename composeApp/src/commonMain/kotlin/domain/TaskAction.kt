package domain

import presentation.screen.home.Tasks

sealed class TaskAction {

    data class Add(val task: TodoTask) : TaskAction()
    data class Update(val task: TodoTask) : TaskAction()
    data class Delete(val task: TodoTask) : TaskAction()
    data class SetCompleted(val task: TodoTask, val completed: Boolean) : TaskAction()
    data class SetFavorite(val task: TodoTask, val isFavorite: Boolean) : TaskAction()
}