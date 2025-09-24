package com.example.cursosescolares.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.model.Course
import com.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import model.Enrollment
import model.Review

class CourseViewModel(
    private val courseRepository: CourseRepository
) : ViewModel() {

    // Flows que el UI consume
    val topCourses: Flow<List<Course>> = courseRepository.getTopCourses()
    val allCourses: Flow<List<Course>> = courseRepository.getAllCourses()
    val enrolledCourses: Flow<List<Course>> = courseRepository.getEnrolledCourses()
    val allReviews: Flow<List<Review>> = courseRepository.getAllReviews()

    // Acciones
    fun enrollCourse(enrollment: Enrollment) = viewModelScope.launch {
        courseRepository.enrollCourse(enrollment)
    }

    fun pauseCourse(enrollmentId: Long) = viewModelScope.launch {
        courseRepository.pauseCourse(enrollmentId)
    }

    fun completeCourse(enrollmentId: Long) = viewModelScope.launch {
        courseRepository.completeCourse(enrollmentId)
    }

    fun addReview(review: Review) = viewModelScope.launch {
        courseRepository.addReview(review)
    }

    fun deleteReview(reviewId: Long) = viewModelScope.launch {
        courseRepository.deleteReview(reviewId)
    }

    fun getReviewsForCourse(courseId: String): Flow<List<Review>> =
        courseRepository.getReviewsForCourse(courseId)

    fun getAverageRatingForCourse(courseId: String): Flow<Float> =
        courseRepository.getAverageRatingForCourse(courseId)

    fun syncWithFirebase() = viewModelScope.launch {
        courseRepository.syncWithFirebase()
    }
}

class CourseViewModelFactory(
    private val courseRepository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(courseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
