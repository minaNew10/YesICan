package com.example.yesican.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yesican.R
import com.example.yesican.databinding.FragmentTasksBinding
import com.example.yesican.util.onQueryTextChanged
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_task,menu)

        val searchItem = menu.findItem(R.id.action_search)
        //this search view is improtant to be androidx
        val searchView = searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            //update search query
            viewModel.searchQuery.value = it
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_date -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.action_hide_completed_tasks ->{
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.action_delete_completed_tasks ->{
                true
            }else -> super.onOptionsItemSelected(item)

        }
    }
}