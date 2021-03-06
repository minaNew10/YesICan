package com.example.yesican.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.yesican.data.PreferencesManager
import com.example.yesican.data.SortOrder
import com.example.yesican.data.Task

import com.example.yesican.data.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao : TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

   val preferencesFlow = preferencesManager.preferencesFlow
    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    private val tasksFlow =  combine(
        searchQuery,
        preferencesFlow
    ){query, filterPreferences->
        Pair(query,filterPreferences)
    }.flatMapLatest {( query,filterPreferences) ->
        taskDao.getTasks(query,filterPreferences.sortOrder,filterPreferences.hideCompleted)
    }
    val tasks = tasksFlow.asLiveData()
    fun onSortOrderSelected(sortOrder: SortOrder) =
        viewModelScope.launch {
            val res = preferencesManager.updateSortOrder(sortOrder)
        }
    fun onHideCompletedClick(hideCompleted: Boolean) =
        viewModelScope.launch {
            val res = preferencesManager.updateHideCompleted(hideCompleted)
        }
    fun onTaskSelected(task:Task){

    }

    fun onTaskCheckedChanged(task: Task, checked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = checked))
    }
    fun onTaskSwiped(task: Task){
        viewModelScope.launch {
            taskDao.delete(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
        }
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    sealed class TasksEvent{
        data class ShowUndoDeleteTaskMessage(val task: Task): TasksEvent()
    }

}


