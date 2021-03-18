package e.raguz.fitme.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {

    @Query("SELECT * from exercise WHERE _id=:id LIMIT 1")
    suspend fun findExerciseById(id: Int): Exercise?

    @get:Query("SELECT * FROM exercise")
    val exercises:List<Exercise>

   @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("DELETE FROM exercise")
    suspend fun deleteAll()


}