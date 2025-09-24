package com.example.cursosescolares.ui.Components

import androidx.compose.runtime.Composable
import com.model.Course

@Composable
fun AnimatedEnrolledCourseCard(
    course: Course,
    onClick: () -> Unit = {},
    onPause: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    // Por ahora, reusa la misma tarjeta simple
    CourseCard(course = course, onClick = onClick)
}
