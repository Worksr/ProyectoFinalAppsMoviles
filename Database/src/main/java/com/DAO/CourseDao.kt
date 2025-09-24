package com.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.model.Course

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses WHERE isPopular = 1 ORDER BY rating DESC LIMIT 10")
    fun getTopCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses ORDER BY title")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<Course>)

    @Query("UPDATE courses SET rating = :rating WHERE id = :courseId")
    suspend fun updateCourseRating(courseId: String, rating: Double)

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: String): Flow<Course>
}


