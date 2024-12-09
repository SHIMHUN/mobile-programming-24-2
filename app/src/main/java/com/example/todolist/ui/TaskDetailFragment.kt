package com.example.todolist.ui

import android.os.Bundle
import android.view.View
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskDetailBinding
import com.example.todolist.ui.common.BaseFragment
import com.example.todolist.ui.common.NavigationUtil.popBackStack
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>(R.layout.fragment_task_detail) {

    private val viewModel: TodoViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedId = requireArguments().getInt("selectedId")
        binding.todoInfo = viewModel.getSelectedTodoInfo(selectedId)
        initViewEventHandler()
    }

    private fun initViewEventHandler() {
        initNavigationEvent()
    }

    private fun initNavigationEvent() {
        binding.btnCancelTask.setOnClickListener { popBackStack() }
    }
}