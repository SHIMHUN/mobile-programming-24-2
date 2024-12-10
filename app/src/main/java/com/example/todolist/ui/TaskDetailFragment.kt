package com.example.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.data.db.TodoInfo
import com.example.todolist.databinding.FragmentTaskDetailBinding
import com.example.todolist.ui.common.BaseFragment
import com.example.todolist.ui.common.NavigationUtil.popBackStack
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Calendar

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>(R.layout.fragment_task_detail) {

    private val viewModel: TodoViewModel by activityViewModel()
    private lateinit var oldSelectedTodoInfo: TodoInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedId = requireArguments().getInt("selectedId")
        viewModel.getSelectedTodoInfo(selectedId)?.let { oldSelectedTodoInfo = it }
        initDataBinding()
        initNavigationEvent()
        handleDateTextClickEvent()
        handleTimeTextClickEvent()
        handleEditButtonClickEvent()
        handleInitButtonClickEvent(oldSelectedTodoInfo)
        handlePriorityTextClickEvent()
        handlePriorityRadioButtonClickEvent()
    }

    private fun initDataBinding() {
        viewModel.selectedTodo.observe(viewLifecycleOwner) {
            binding.todoInfo = it
        }
        binding.oldTodoInfo = oldSelectedTodoInfo
        binding.isClickedPriorityText = false
    }

    private fun initNavigationEvent() {
        binding.btnCancelTask.setOnClickListener { popBackStack() }
    }

    private fun handleInitButtonClickEvent(oldSelectedTodoInfo: TodoInfo) {
        binding.btnInitTask.setOnClickListener {
            initPriorityRadioGroupVisibility()
            viewModel.initSelectedTodoInfo(oldSelectedTodoInfo)
            binding.oldTodoInfo = oldSelectedTodoInfo
        }
    }

    private fun handleEditButtonClickEvent() {
        binding.btnEditTask.setOnClickListener {
            lifecycleScope.launch {
                val isSucceed = viewModel.updateTodoData(
                    binding.etvTaskDetailTitle.text.toString(),
                    binding.etvTaskDetailDescription.text.toString(),
                    binding.etvTaskDetailDate.text.toString(),
                    binding.etvTaskDetailTime.text.toString(),
                    binding.etvTaskDetailPriority.text.toString(),
                    oldSelectedTodoInfo
                )
                if(isSucceed) {
                    Toast.makeText(requireActivity(), "수정됐습니다", Toast.LENGTH_SHORT).show()
                    popBackStack()
                }
                else { Toast.makeText(requireActivity(), "오류입니다", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun handleDateTextClickEvent() {
        binding.etvTaskDetailDate.setOnClickListener { showDatePicker() }
    }

    private fun handleTimeTextClickEvent() {
        binding.etvTaskDetailTime.setOnClickListener { showTimePicker() }
    }

    private fun handlePriorityTextClickEvent() {
        binding.etvTaskDetailPriority.setOnClickListener {
            if(binding.rgPriority.visibility == View.VISIBLE) binding.rgPriority.clearCheck()
            binding.isClickedPriorityText = !binding.rgPriority.isVisible
        }
    }

    private fun handlePriorityRadioButtonClickEvent() {
        binding.rgPriority.setOnCheckedChangeListener { _, selectedRadioButtonId ->
            binding.root.findViewById<RadioButton>(selectedRadioButtonId)?.let {
                val selectedButton = it
                viewModel.updatePriorityOfSelectedTodoInfo(selectedButton.text.toString())
                initPriorityRadioGroupVisibility()
            }
        }
    }

    private fun initPriorityRadioGroupVisibility() {
        binding.isClickedPriorityText = false
        binding.rgPriority.clearCheck()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                viewModel.updateDateOfSelectedTodoInfo(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                viewModel.updateTimeOfSelectedTodoInfo(selectedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }
}