package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TaskDetailFragment : Fragment() {

    // UI 요소
    private lateinit var tvTaskDetailHeader: TextView
    private lateinit var tvTaskDescription: TextView
    private lateinit var tvTaskDate: TextView
    private lateinit var tvTaskTime: TextView
    private lateinit var tvTaskPriority: TextView
    private lateinit var btnEditTask: Button

    // 전달받은 데이터
    private var taskId: Int? = null
    private var taskTitle: String? = null
    private var taskDescription: String? = null
    private var taskDate: String? = null
    private var taskTime: String? = null
    private var taskPriority: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.taskdetail, container, false)

        // 뷰 초기화
        tvTaskDetailHeader = view.findViewById(R.id.tv_task_detail_header)
        tvTaskDescription = view.findViewById(R.id.tv_task_detail_description)
        tvTaskDate = view.findViewById(R.id.tv_task_detail_date)
        tvTaskTime = view.findViewById(R.id.tv_task_detail_time)
        tvTaskPriority = view.findViewById(R.id.tv_task_detail_priority)
        btnEditTask = view.findViewById(R.id.btn_edit_task)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터 가져오기
        arguments?.let {
            taskId = it.getInt("taskId")
            taskTitle = it.getString("taskTitle")
            taskDescription = it.getString("taskDescription")
            taskDate = it.getString("taskDate")
            taskTime = it.getString("taskTime")
            taskPriority = it.getString("taskPriority")
        }

        // UI 업데이트
        updateTaskDetails()

        // 수정 버튼 클릭 리스너
        btnEditTask.setOnClickListener {
            val fragment = TaskFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("isEditMode", true)
                    putInt("taskId", taskId ?: -1)
                    putString("taskTitle", taskTitle)
                    putString("taskDescription", taskDescription)
                    putString("taskDate", taskDate)
                    putString("taskTime", taskTime)
                    putString("taskPriority", taskPriority)
                }
            }

            // TaskFragment로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // 할일 세부 정보를 UI에 업데이트
    private fun updateTaskDetails() {
        tvTaskDetailHeader.text = taskTitle ?: "제목 없음"
        tvTaskDescription.text = taskDescription ?: "내용 없음"
        tvTaskDate.text = taskDate ?: "날짜 없음"
        tvTaskTime.text = taskTime ?: "시간 없음"
        tvTaskPriority.text = taskPriority ?: "우선순위 없음"
    }
}