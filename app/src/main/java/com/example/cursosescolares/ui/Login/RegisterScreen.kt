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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun RegisterScreen(nav: NavHostController) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Validaciones
    val hasAt = email.contains("@")
    val emailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val showEmailAtErr = email.isNotEmpty() && !hasAt
    val showEmailPatternErr = email.isNotEmpty() && hasAt && !emailValid

    val passMinLen = pass.length >= 6
    val samePass = pass == confirm
    val formOk = emailValid && passMinLen && samePass

    val auth = remember { FirebaseAuth.getInstance() }

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
            Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.replace(" ", "") },
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = showEmailAtErr || showEmailPatternErr,
                supportingText = {
                    when {
                        showEmailAtErr -> Text("El correo debe incluir @")
                        showEmailPatternErr -> Text("Formato de correo inválido")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Contraseña (oculta)
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña (mín. 6)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = pass.isNotEmpty() && !passMinLen,
                supportingText = {
                    if (pass.isNotEmpty() && !passMinLen) Text("Debe tener al menos 6 caracteres")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Confirmación (oculta)
            OutlinedTextField(
                value = confirm,
                onValueChange = { confirm = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirm.isNotEmpty() && !samePass,
                supportingText = {
                    if (confirm.isNotEmpty() && !samePass) Text("Las contraseñas no coinciden")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMsg != null) {
                Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
            }

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    errorMsg = null
                    isLoading = true
                    scope.launch {
                        try {
                            auth.createUserWithEmailAndPassword(email, pass).await()

                            // ✅ ÉXITO: ir a Login y quitar Register del back stack
                            nav.navigate("login") {
                                popUpTo("register") { inclusive = true } // elimina Register de la pila
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            // ... (tu manejo de errores)
                            val msg = e.message ?: "Error desconocido"
                            errorMsg = when {
                                msg.contains("email address is already in use", ignoreCase = true) ->
                                    "Ese correo ya está registrado."
                                msg.contains("operation not allowed", ignoreCase = true) ->
                                    "Habilita 'Email/Password' en Firebase Authentication."
                                msg.contains("badly formatted", ignoreCase = true) ->
                                    "El correo tiene un formato inválido."
                                msg.contains("Password should be at least", ignoreCase = true) ->
                                    "La contraseña es demasiado débil (mínimo 6 caracteres)."
                                else -> msg
                            }
                        } finally {
                            isLoading = false
                        }

                    }
                },
                enabled = formOk && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (isLoading) "Creando..." else "Crear cuenta")
            }


            TextButton(
                onClick = { nav.popBackStack() },
            ) { Text("¿Ya tienes cuenta? Inicia sesión") }
        }
    }
}