package com.example.cursosescolares.ui.Screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cursosescolares.ViewModel.CourseViewModel
import com.example.cursosescolares.ui.Components.CourseCard

@Composable
fun TopCoursesScreen(
    viewModel: CourseViewModel,
    onCourseClick: (String) -> Unit
) {
    val list by viewModel.topCourses.collectAsState(initial = emptyList())
    LazyColumn {
        items(list) { course ->
            CourseCard(course = course) {
                onCourseClick(course.id)
            }
        }
    }
}
