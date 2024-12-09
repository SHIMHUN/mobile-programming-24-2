package com.example.todolist.ui

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.db.TodoInfo
import com.example.todolist.databinding.ListItemTodoBinding
import com.example.todolist.ui.common.AdapterListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoAdapter(private val navigationListener: AdapterListener) :
    ListAdapter<TodoInfo, TodoAdapter.TodoViewHolder>(DiffCallback()) {

    inner class TodoViewHolder(private val binding: ListItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoInfo) {
            binding.todoInfo = todoItem
            handleContainerClickEvent(todoItem)
            handleDeleteBtnClickEvent(todoItem)
        }

        private fun handleContainerClickEvent(todoItem: TodoInfo) {
            binding.llTodoContainer.setOnClickListener {
                navigationListener.navigateToTaskDetail(todoItem)
            }
        }

        private fun handleDeleteBtnClickEvent(todoItem: TodoInfo) {
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("네", { _, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            if (navigationListener.requestToDeleteItem(todoItem)) {
                                Toast.makeText(binding.root.context, "삭제됐습니다", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(binding.root.context, "오류입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                    .setNegativeButton("아니오") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback() : DiffUtil.ItemCallback<TodoInfo>() {
    override fun areItemsTheSame(oldItem: TodoInfo, newItem: TodoInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoInfo, newItem: TodoInfo): Boolean {
        return oldItem == newItem
    }

}