package com.example.cursosescolares.ui.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.model.Course
import model.Enrollment

/**
 * Pantalla de inscripción SIN dependencia directa de Repository/ViewModel.
 * El que la llame puede capturar el resultado vía onSubmit(enrollment).
 */
@Composable
fun EnrollmentFormScreen(
    course: Course,
    onSubmit: (Enrollment) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Inscripción al curso",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = course.title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Tarjeta (últimos 4)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                // Validaciones muy básicas
                if (fullName.isBlank() || email.isBlank() || cardNumber.length < 4) {
                    error = "Completa los campos y verifica la tarjeta."
                    return@Button
                }
                error = null
                val enrollment = Enrollment(
                    id = 0L,                 // lo asignará tu capa de datos si quieres
                    courseId = course.id,
                    userId = "",             // puedes pasar el UID de FirebaseAuth si lo manejas arriba
                    status = "enrolled"
                )
                onSubmit(enrollment)        // el caller decide qué hacer (guardar, navegar, etc.)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar inscripción")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}
