package com.example.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.ui.common.BaseFragment
import com.example.todolist.ui.common.NavigationUtil.popBackStack
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Calendar

class TaskFragment : BaseFragment<FragmentTaskBinding>(R.layout.fragment_task) {

    private val viewModel: TodoViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTaskDate.setOnClickListener { showDatePicker() }
        binding.btnTaskTime.setOnClickListener { showTimePicker() }
        binding.btnSaveTask.setOnClickListener {
            lifecycleScope.launch {
                if(addTodoData()) {
                    Toast.makeText(requireActivity(), "추가됐습니다", Toast.LENGTH_SHORT).show()
                    popBackStack()
                }
                else {
                    Toast.makeText(requireActivity(), "오류입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                binding.date = selectedDate
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
                binding.time = selectedTime
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun getPriority(): String {
        val selectedRadioButtonId = binding.radioGroupPriority.checkedRadioButtonId
        return binding.root.findViewById<RadioButton>(selectedRadioButtonId).text.toString()
    }

    private suspend fun addTodoData(): Boolean {
        Log.d("TaskFragment", "addTodoData:")
        return viewModel.addTodoData(
            binding.etTaskTitle.text.toString(),
            binding.etTaskDescription.text.toString(),
            binding.tvDate.text.toString(),
            binding.tvTime.text.toString(),
            getPriority()
        )
    }
}