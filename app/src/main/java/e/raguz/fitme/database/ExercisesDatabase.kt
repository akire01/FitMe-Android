package e.raguz.fitme.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDao

@Database(entities = [Exercise::class], version = 1)
abstract class ExercisesDatabase : RoomDatabase() {

    abstract fun exerciseDao() : ExerciseDao

    companion object{
        private var INSTANCE: ExercisesDatabase? = null
        private const val DB_NAME = "exercises.db"

        fun getDatabase(context: Context): ExercisesDatabase{
            if (INSTANCE == null){
                synchronized(ExercisesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, ExercisesDatabase::class.java, DB_NAME)
                            //.allowMainThreadQueries() // Uncomment if you don't want to use RxJava or coroutines just yet (blocks UI thread)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    Log.d("ExercisesDatabase", "making...")
                                    //GlobalScope.launch(Dispatchers.IO) { rePopulateDb(INSTANCE) }
                                }
                            }).build()
                    }
                }
            }
            return INSTANCE!!
        }

    }
}