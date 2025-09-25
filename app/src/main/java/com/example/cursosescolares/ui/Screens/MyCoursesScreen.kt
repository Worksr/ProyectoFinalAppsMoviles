package com.example.cursosescolares.ui.Screens

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.cursosescolares.ViewModel.CourseViewModel
import com.example.cursosescolares.ui.Components.AnimatedEnrolledCourseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    viewModel: CourseViewModel,
    onCourseClick: (String) -> Unit,
    onOpenSettings: () -> Unit = {}        // <— NUEVO
) {
    val list by viewModel.enrolledCourses.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text("Mis cursos") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onOpenSettings) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                }
            )
        }
    ) { padding ->
        // Tu lista actual (la dejamos igual)
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = list) { course ->
                AnimatedEnrolledCourseCard(
                    course = course,
                    onClick = { onCourseClick(course.id) },
                    onPause = { /* si aplicara */ },
                    onComplete = { /* si aplicara */ }
                )
            }
        }
    }
}
