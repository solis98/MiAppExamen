package com.example.miappexamen.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.miappexamen.data.remote.FirebaseAuthManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var passwordVisible by rememberSaveable { mutableStateOf(false)}

    val context = LocalContext.current

    val formComplete = email.isNotBlank() &&
            password.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            label = { Text("Correo electrónico")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text("Contraseña")},
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextButton(
            onClick = { passwordVisible = !passwordVisible}
        ) {
            Text(
                if (passwordVisible){
                    "Ocultar contraseña"
                } else{
                    "Mostrar contraseña"
                }
            )
        }

        Button(
            onClick = {
                isLoading = true

                scope.launch {
                    try {
                        val result = FirebaseAuthManager.loginUser(
                            email = email.trim(),
                            password = password
                        )

                        if (result.isSuccess) {
                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                result.exceptionOrNull()?.message
                                    ?: "No se pudo iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = formComplete && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isLoading) "Ingresando..." else "Ingresar"
            )
        }

    }

    TextButton(
        enabled = !isLoading,
        onClick = {
            navController.navigate("register") {
                launchSingleTop = true
            }
        }
    ) {
        Text("Crear cuenta")
    }
}