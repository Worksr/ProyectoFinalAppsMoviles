package com.example.cursosescolares.ui.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cursosescolares.ViewModel.CourseViewModel
import model.Review

/**
 * Pantalla de reseñas SIN dependencia directa de Repository.
 * Recibe el ViewModel y el courseId.
 */
@Composable
fun ReviewsScreen(
    viewModel: CourseViewModel,
    courseId: String
) {
    // Cargamos las reseñas del curso
    val reviews by viewModel.getReviewsForCourse(courseId).collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Reseñas",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(reviews) { review ->
                ReviewCard(review = review)
            }

            if (reviews.isEmpty()) {
                item {
                    Text(
                        text = "Aún no hay reseñas para este curso.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar reseña")
        }
    }

    if (showAddDialog) {
        AddReviewDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { rating, comment ->
                // Crea un Review mínimo y mándalo al VM
                val newReview = Review(
                    id = 0L,
                    courseId = courseId,
                    userId = "", // si tienes FirebaseAuth arriba, pásalo ahí
                    rating = rating,
                    comment = comment
                )
                viewModel.addReview(newReview)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "⭐ ${"%.1f".format(review.rating)}",
                style = MaterialTheme.typography.titleMedium
            )
            if (review.comment.isNotBlank()) {
                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Diálogo simple para capturar rating (0–5) y comentario.
 * Lo dejamos aquí mismo para evitar referencias no resueltas.
 */
@Composable
private fun AddReviewDialog(
    onDismiss: () -> Unit,
    onConfirm: (Float, String) -> Unit
) {
    var rating by remember { mutableFloatStateOf(5f) }
    var comment by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva reseña") },
        text = {
            Column {
                OutlinedTextField(
                    value = rating.toString(),
                    onValueChange = { text ->
                        val v = text.toFloatOrNull()
                        if (v != null) rating = v
                    },
                    label = { Text("Calificación (0 a 5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comentario (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (rating < 0f || rating > 5f) {
                    error = "La calificación debe estar entre 0 y 5."
                    return@TextButton
                }
                error = null
                onConfirm(rating, comment)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
