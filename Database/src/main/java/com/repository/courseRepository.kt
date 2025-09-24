package com.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import com.dao.CourseDao
import model.Enrollment
import model.Review

class CourseRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: CourseDao? = null
) {
    private val courses = db.collection("courses")

    // --- Firestore básicos que ya tenías ---
    suspend fun getCoursesOnce(): List<Course> {
        val snap = courses.get().await()
        return snap.documents.mapNotNull { it.toObject(Course::class.java)?.copy(id = it.id) }
    }

    suspend fun getCourseOnce(id: String): Course? {
        val doc = courses.document(id).get().await()
        return doc.toObject(Course::class.java)?.copy(id = doc.id)
    }

    suspend fun addCourse(course: Course): String {
        val ref = if (course.id.isBlank()) {
            courses.add(course.copy(id = "")).await()
        } else {
            courses.document(course.id).set(course).await()
            courses.document(course.id)
        }
        return ref.id
    }

    suspend fun updateCourse(course: Course) {
        require(course.id.isNotBlank())
        courses.document(course.id).set(course).await()
    }

    suspend fun deleteCourse(id: String) {
        courses.document(id).delete().await()
    }

    suspend fun updateCourseRating(id: String, newRating: Double) {
        val docRef = courses.document(id)
        db.runTransaction { tx ->
            val snap = tx.get(docRef)
            val current = snap.toObject(Course::class.java)
            val currentCount = current?.ratingsCount ?: 0
            val currentAvg   = current?.rating ?: 0.0
            val total = currentAvg * currentCount + newRating
            val count = currentCount + 1
            val avg   = if (count > 0) total / count else 0.0
            tx.update(docRef, mapOf("rating" to avg, "ratingsCount" to count))
            null
        }.await()
    }

    // --- Flows que pide el ViewModel (usa Room si hay dao, si no, vacío) ---
    fun getTopCourses(): Flow<List<Course>> = dao?.getTopCourses() ?: flowOf(emptyList())
    fun getAllCourses(): Flow<List<Course>> = dao?.getAllCourses() ?: flowOf(emptyList())
    fun getEnrolledCourses(): Flow<List<Course>> = flowOf(emptyList())

    fun getAllReviews(): Flow<List<Review>> = flowOf(emptyList())
    fun getReviewsForCourse(courseId: String): Flow<List<Review>> = flowOf(emptyList())
    fun getAverageRatingForCourse(courseId: String): Flow<Float> = flowOf(0f)

    // --- Acciones (stubs para compilar) ---
    suspend fun enrollCourse(enrollment: Enrollment) { /* TODO persistir */ }
    suspend fun pauseCourse(enrollmentId: Long) { /* TODO persistir */ }
    suspend fun completeCourse(enrollmentId: Long) { /* TODO persistir */ }
    suspend fun addReview(review: Review) { /* TODO persistir */ }
    suspend fun deleteReview(reviewId: Long) { /* TODO persistir */ }

    // --- Sincronización (stub) ---
    suspend fun syncWithFirebase() { /* TODO: Room <-> Firestore */ }
}


