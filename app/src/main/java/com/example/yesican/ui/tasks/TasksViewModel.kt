package com.example.yesican.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.yesican.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao : TaskDao
) : ViewModel() {
}