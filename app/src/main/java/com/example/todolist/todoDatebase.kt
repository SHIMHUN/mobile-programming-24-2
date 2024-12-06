import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.TodoInfo

@Database(entities = [TodoInfo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao


    //companion object를 만들어서 getInstance라는 메소드만 호출하면 바로 불러올수있도록
    companion object {
        private var instance : TodoDatabase? = null
        @Synchronized
        fun getInstance(context : Context) : TodoDatabase? {
            if (instance == null) {
                synchronized((TodoDatabase :: class)) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase :: class.java,
                        "todo-database"
                    ).build()
                }
            }
            return instance
        }
    }

}