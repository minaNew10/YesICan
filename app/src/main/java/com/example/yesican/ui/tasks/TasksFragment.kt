package com.example.yesican.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yesican.R
import com.example.yesican.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment :Fragment(R.layout.fragment_tasks){
    private  val viewModel : TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //here we don't call inflate because the layout is already inflated in the constructor
        val binding = FragmentTasksBinding.bind(view)
        val tasksAdapter = TasksAdapter()
        binding.apply {
            recyclerViewTasks.apply {
                adapter = tasksAdapter
                setHasFixedSize(true)
            }
        }
        viewModel.tasks.observe(viewLifecycleOwner){
            it?.let {
                tasksAdapter.submitList(it)
            }
        }
    }
}