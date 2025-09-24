package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.model.Course
import kotlinx.coroutines.flow.Flow
import model.Enrollment

@Dao
interface EnrollmentDao {
    @Query("SELECT * FROM enrollments WHERE isActive = 1")
    fun getActiveEnrollments(): Flow<List<Enrollment>>

    @Insert
    suspend fun insertEnrollment(enrollment: Enrollment): Long

    @Query("UPDATE enrollments SET isActive = isActive WHERE id = :enrollmentId")
    suspend fun updateEnrollmentStatus(enrollmentId: Long, isActive: Boolean)

    @Query("DELETE FROM enrollments WHERE id = :enrollmentId")
    suspend fun deleteEnrollment(enrollmentId: Long)

    @Transaction
    @Query("SELECT courses.* FROM courses INNER JOIN enrollments ON courses.id = enrollments.courseId WHERE enrollments.isActive = 1")
    fun getEnrolledCourses(): Flow<List<Course>>
}