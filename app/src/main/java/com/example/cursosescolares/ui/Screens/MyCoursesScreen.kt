package com.example.cursosescolares.ui.Screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cursosescolares.ViewModel.CourseViewModel
import com.example.cursosescolares.ui.Components.AnimatedEnrolledCourseCard

@Composable
fun MyCoursesScreen(
    viewModel: CourseViewModel,
    onCourseClick: (String) -> Unit
) {
    val list by viewModel.enrolledCourses.collectAsState(initial = emptyList())

    LazyColumn {
        items(list) { course ->
            AnimatedEnrolledCourseCard(
                course = course,
                onClick = { onCourseClick(course.id) },
                onPause = { /* viewModel.pauseCourse(id) si tuvieras id long */ },
                onComplete = { /* viewModel.completeCourse(id) */ }
            )
        }
    }
}
