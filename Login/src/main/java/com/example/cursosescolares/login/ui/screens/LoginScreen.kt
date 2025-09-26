package com.example.cursosescolares.login.ui.screens

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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(nav: NavHostController) {
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Validaciones
    val emailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passOk = pass.isNotBlank()           // cámbialo a length>=6 si quieres
    val formOk = emailValid && passOk && !isLoading

// Si ya hay usuario logeado, salta login
    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            nav.navigate("mycourses") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

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
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim().lowercase().replace(" ", "") },
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = email.isNotEmpty() && !emailValid,
                supportingText = {
                    if (email.isNotEmpty() && !emailValid) Text("Correo inválido (debe incluir @)")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMsg != null) {
                Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    errorMsg = null
                    isLoading = true
                    scope.launch {
                        try {
                            auth.signInWithEmailAndPassword(
                                email.trim().lowercase(),
                                pass
                            ).await()

                            nav.navigate("mycourses") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            val msg = e.message ?: "Error al iniciar sesión"
                            errorMsg = when {
                                msg.contains("There is no user record", true) ->
                                    "No existe una cuenta con ese correo."
                                msg.contains("The password is invalid", true) ||
                                        msg.contains("INVALID_LOGIN_CREDENTIALS", true) ->
                                    "Contraseña incorrecta."
                                msg.contains("blocked all requests", true) ->
                                    "Demasiados intentos. Intenta más tarde."
                                else -> "No se pudo iniciar sesión: $msg"
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = formOk,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (isLoading) "Entrando..." else "Entrar")
            }

            TextButton(onClick = { nav.navigate("register") }) {
                Text("¿No tienes cuenta? Crea una")
            }
        }
    }
}
