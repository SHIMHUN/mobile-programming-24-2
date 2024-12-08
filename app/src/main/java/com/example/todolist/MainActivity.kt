package com.example.todolist

//import TodoDatabase
//import com.example.todolist.TodoDatabase
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogEditBinding
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding   //binding 준비
    private lateinit var todoAdapter: TodoAdapter       //adapter 준비
    private lateinit var roomDatabase: TodoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("MainActivity","test1")
        // 어뎁터 생성
        todoAdapter = TodoAdapter()

        Log.i("MainActivity","test2")
        // 리사이클러뷰에 어뎁터 세팅
        binding.recyclerviewTodo.adapter = todoAdapter


        Log.i("MainActivity","test3")
//        // roomDatabase 초기화
        TodoDatabase.getInstance(applicationContext).let { roomDatabase = it }

        Log.i("MainActivity","test4")
        // 전체 데이터 load(가져옴) (코루틴 사용)
        CoroutineScope(Dispatchers.IO).launch {
            val lstTodo = roomDatabase.todoDao().getAllReadData() as ArrayList<TodoInfo> //전체데이터 가져옴
            //어뎁터쪽에 데이터 전달
            for (todoItem in lstTodo) {
                todoAdapter.addListItem(todoItem)
            }
            // UI Thread에서 처리
            runOnUiThread {
                todoAdapter.notifyDataSetChanged()
            }
        }

        Log.i("MainActivity","test5")
        // 플로팅 작성하기 버튼 클릭했을때 (AlerDialg로 팝업창 만들기)
        binding.btnWrite.setOnClickListener {
            val bindingDialog = DialogEditBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root,
                false
            )

            AlertDialog.Builder(this)
                .setTitle("할일 기록하기")
                .setView(bindingDialog.root)
                .setPositiveButton("작성완료") { _, _ ->
                    // 작성 완료 버튼 눌렀을 때
                    val todoContent = bindingDialog.etMemo.text.toString()
                    val todoDate = SimpleDateFormat("yyyy-MM-dd").format(Date())

                    // TodoInfo 객체 생성 시 속성 초기화
                    val todoItem = TodoInfo(todoContent = todoContent, todoDate = todoDate)

                    // Adapter에 추가
                    todoAdapter.addListItem(todoItem)

                    // 데이터베이스에 삽입
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDatabase.todoDao().insertTodoData(todoItem)
                        runOnUiThread {
                            todoAdapter.notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }
}