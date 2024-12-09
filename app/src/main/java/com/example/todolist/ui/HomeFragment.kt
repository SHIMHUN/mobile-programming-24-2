package com.example.todolist.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.todolist.R
import com.example.todolist.data.db.TodoInfo
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.ui.common.AdapterListener
import com.example.todolist.ui.common.BaseFragment
import com.example.todolist.ui.common.NavigationUtil.navigate
import com.example.todolist.ui.common.NavigationUtil.navigateWithArgs
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), AdapterListener {

    private val viewModel: TodoViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestTodoList()
        initViewEventHandler()
        initRecyclerView()
    }

    private fun initViewEventHandler() {
        initNavigationEvent()
    }

    private fun initNavigationEvent() {
        binding.btnWrite.setOnClickListener { navigate(R.id.action_homeFragment_to_taskFragment) }
    }

    private fun initRecyclerView() {
        val adapter = TodoAdapter(this)
        binding.recyclerviewTodo.adapter = adapter
        viewModel.todoList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun navigateToTaskDetail(todoInfo: TodoInfo) {
        viewModel.getSelectedTodoInfo(todoInfo.id)?.let {
            val bundle = bundleOf("selectedId" to todoInfo.id)
            navigateWithArgs(R.id.action_homeFragment_to_taskDetailFragment, bundle)
        } ?: Toast.makeText(requireContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
    }

    override suspend fun requestToDeleteItem(item: TodoInfo): Boolean {
        return viewModel.deleteTodoData(item)
    }
}



