package com.example.cursosescolares.ui.Login

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(nav: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Reglas de validación
    val hasAt = email.contains("@")
    val emailPatternOk = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val showEmailErrorAt = email.isNotEmpty() && !hasAt
    val showEmailPatternError = email.isNotEmpty() && hasAt && !emailPatternOk

    // Reglas de contraseña (oculta SIEMPRE)
    val passwordOk = password.isNotBlank() // o: password.length >= 6

    val formOk = emailPatternOk && passwordOk

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar sesión", style = MaterialTheme.typography.headlineSmall)

            // Campo email (obliga a incluir '@' y formato de correo)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.replace(" ", "") },
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = showEmailErrorAt || showEmailPatternError,
                supportingText = {
                    when {
                        showEmailErrorAt -> Text("El correo debe incluir @")
                        showEmailPatternError -> Text("Formato de correo inválido")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Campo contraseña (siempre en puntos)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Botón: navega a tu pantalla inicial después de login (ajusta la ruta si quieres "top"/"all"/"mine")
            Button(
                onClick = {
                    nav.navigate("top") { // 👈 cambia a "all" o "mine" si prefieres
                        popUpTo("login") { inclusive = true }
                    }
                },
                enabled = formOk,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Entrar")
            }
        }
    }
}
