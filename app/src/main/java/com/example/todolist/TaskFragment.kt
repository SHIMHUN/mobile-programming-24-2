package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TaskFragment : Fragment() {

    // 뷰 요소 선언
    private lateinit var etTaskTitle: TextInputEditText
    private lateinit var etTaskDescription: TextInputEditText
    private lateinit var btnTaskDate: MaterialButton
    private lateinit var btnTaskTime: MaterialButton
    private lateinit var btnSaveTask: MaterialButton
    private lateinit var tvSelectedDateTime: TextView
    private lateinit var rgPriority: RadioGroup


    // 선택된 날짜와 시간 저장 변수
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedPriority: String = "상"

    // 수정 모드 플래그와 기존 데이터
    private var isEditMode: Boolean = false
    private var taskId: Int? = null // 수정할 데이터의 ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // task.xml 레이아웃을 inflate
        val view = inflater.inflate(R.layout.task, container, false)

        // 뷰 초기화
        etTaskTitle = view.findViewById(R.id.et_task_title)
        etTaskDescription = view.findViewById(R.id.et_task_description)
        btnTaskDate = view.findViewById(R.id.btn_task_date)
        btnTaskTime = view.findViewById(R.id.btn_task_time)
        btnSaveTask = view.findViewById(R.id.btn_save_task)
        tvSelectedDateTime = view.findViewById(R.id.tv_selected_date_time)
        rgPriority = view.findViewById(R.id.radio_group_priority)

        return view
    }

    // 수정 모드 데이터 받기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터로 수정 모드인지 확인
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            if (isEditMode) {
                taskId = it.getInt("taskId")
                etTaskTitle.setText(it.getString("taskTitle", ""))
                etTaskDescription.setText(it.getString("taskDescription", ""))
                selectedDate = it.getString("taskDate", "")
                selectedTime = it.getString("taskTime", "")
                selectedPriority = it.getString("taskPriority", "상")
                btnTaskDate.text = selectedDate
                btnTaskTime.text = selectedTime
                updateSelectedDateTime()
                // 선택된 중요도 설정
                when (selectedPriority) {
                    "상" -> view.findViewById<RadioButton>(R.id.rb_high).isChecked = true
                    "중" -> view.findViewById<RadioButton>(R.id.rb_medium).isChecked = true
                    "하" -> view.findViewById<RadioButton>(R.id.rb_low).isChecked = true
                }

                btnSaveTask.text = "수정 완료"
            }
        }

        // 날짜 선택 버튼 클릭 리스너 설정
        btnTaskDate.setOnClickListener{ showDatePicker() }

        // 시간 선택 버튼 클릭 리스너 설정
        btnTaskTime.setOnClickListener{ showTimePicker() }

        // 저장 버튼 클릭 리스너 설정
        btnSaveTask.setOnClickListener{ saveTask() }
    }
    // 날짜 선택 다이얼로그 표시
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // 선택된 날짜 저장
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                btnTaskDate.text = selectedDate // 버튼 텍스트 업데이트
                updateSelectedDateTime() // 날짜와 시간 표시 업데이트
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // 시간 선택 다이얼로그 표시
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // 선택된 시간 저장
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                btnTaskTime.text = selectedTime // 버튼 텍스트 업데이트
                updateSelectedDateTime() // 날짜와 시간 표시 업데이트
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    // 선택된 날짜와 시간 TextView 업데이트
    private fun updateSelectedDateTime() {
        val dateTimeText = if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            "$selectedDate $selectedTime" // 선택된 날짜와 시간을 표시
        } else {
            "____Y__M__D __:__" // 기본 텍스트
        }
        tvSelectedDateTime.text = dateTimeText
    }

    // 할 일 저장
    private fun saveTask() {
        val title = etTaskTitle.text.toString()
        val description = etTaskDescription.text.toString()

        // 중요도 값 확인
        val selectedRadioButtonId = rgPriority.checkedRadioButtonId
        selectedPriority = view?.findViewById<RadioButton>(selectedRadioButtonId)?.text.toString()

        // 필수 입력값 검증
        if (title.isBlank() || description.isBlank() || selectedDate.isBlank() || selectedTime.isBlank() || selectedPriority.isBlank()) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 비동기 작업으로 저장 처리
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (isEditMode) {
                    // 기존 할일 업데이트 로직
                    updateTask(taskId, title, description, selectedDate, selectedTime, selectedPriority)
                } else {
                    // 새 할일 저장 로직
                    saveNewTask(title, description, selectedDate, selectedTime, selectedPriority)
                }
            }
            withContext(Dispatchers.Main) {
                val message = if (isEditMode) "할 일이 수정되었습니다." else "할 일이 추가되었습니다."
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun saveNewTask(title: String, description: String, date: String, time: String, priority: String) {
        // 새 할일 저장 로직 구현 (DB)
    }

    private fun updateTask(id: Int?, title: String, description: String, date: String, time: String, priority: String) {
        // 기존 할일 업데이트 로직 구현 (DB)
    }
}