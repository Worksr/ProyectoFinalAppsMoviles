package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import model.Review

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews ORDER BY date DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELCT * FROM reviews WHERE courseId = :courseId ORDER BY date DESC")
    fun getReviewsForCourse(courseId: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // add OnConflictStrategy.REPLACE al graddle
    suspend fun insert(review: Review)

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReview(reviewId: Long)

    @Query("SELECT AVG(rating) FROM reviews WHERE courseId = :courseId")
    fun getAverageRatingForCourse(courseId: String): Flow<Float?>
}