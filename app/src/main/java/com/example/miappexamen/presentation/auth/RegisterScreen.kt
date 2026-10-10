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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                Toast.makeText(
                    context,
                    "Formulario completo. Conectaremos Firebase después",
                    Toast.LENGTH_SHORT
                ).show()
            },
            enabled = formComplete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }


    }

    TextButton(
        onClick = {
            navController.popBackStack()
        }
    ) {
        Text("Ya tengo cuenta")
    }

}