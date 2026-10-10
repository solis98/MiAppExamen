package com.example.miappexamen.presentation.auth

import android.util.Patterns
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
fun RegisterScreen(navController: NavController) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false)}

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val formComplete = fullName.isNotBlank() &&
            email.isNotBlank() &&
            dob.isNotBlank() &&
            phoneNumber.isNotBlank() &&
            password.isNotBlank()

    Column(
        modifier =  Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registro de usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it},
            label = { Text("Nombre completo")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            label = { Text("Correo electónico")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it},
            label = { Text("Fecha de nacimiento")},
            placeholder = { Text("DD/MM/AAAA")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it},
            label = { Text("Número de teléfono")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text("Contraseña")},
            visualTransformation = if(passwordVisible){
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
            onClick = { passwordVisible = !passwordVisible }
        ) {
            Text(
                if (passwordVisible) {
                    "Ocultar contraseña"
                } else {
                    "Mostrar contraseña"
                }
            )
        }

        Button(
            onClick = {
                val cleanEmail = email.trim()

                when{
                    !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                        Toast.makeText(
                            context,
                            "ingresar un correo válido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    password.length < 6 -> {
                        Toast.makeText(
                            context,
                            "La contraseña debe tener al menos 6 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        isLoading = true

                        scope.launch {
                            try {
                                val result = FirebaseAuthManager.registerUser(
                                    name = fullName.trim(),
                                    email = cleanEmail,
                                    password = password,
                                    dob = dob.trim(),
                                    phoneNumber = phoneNumber.trim()
                                )

                                if (result.isSuccess){
                                    Toast.makeText(
                                        context,
                                        "Registro exitoso. Ahora inicia sesión",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.popBackStack()
                                } else{
                                    Toast.makeText(
                                        context,
                                        result.exceptionOrNull()?.message
                                            ?: "No se pudo completar el registro",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }
            },
            enabled = formComplete && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isLoading) "Registrando..." else "Registrar"
            )
        }

        TextButton(
            enabled = !isLoading,
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Ya tengo cuenta")
        }


    }



}