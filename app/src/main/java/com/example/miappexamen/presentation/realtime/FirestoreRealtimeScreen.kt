package com.example.miappexamen.presentation.realtime

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun FirestoreRealtimeScreen() {
    val context = LocalContext.current

    val document = remember {
        FirebaseFirestore.getInstance()
            .collection("demo")
            .document("test")
    }

    var contador by remember { mutableStateOf(0L) }
    var mensaje by remember { mutableStateOf("") }
    var mensajeUsuario by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Cargando datos...") }
    var guardando by remember { mutableStateOf(false) }

    DisposableEffect(document) {
        val listener = document.addSnapshotListener { snapshot, error ->
            if (error != null) {
                estado = "Error: ${error.message}"
                return@addSnapshotListener
            }

            if (snapshot != null) {
                contador = snapshot.getLong("contador") ?: 0L
                mensaje = snapshot.getString("mensaje") ?: ""

                estado = if (snapshot.metadata.isFromCache) {
                    "Datos locales; esperando al servidor"
                } else {
                    "Datos recibidos del servidor"
                }
            }
        }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Firestore en tiempo real",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = estado,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Contador: $contador",
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            enabled = !guardando,
            onClick = {
                guardando = true

                document.set(
                    mapOf("contador" to FieldValue.increment(1L)),
                    SetOptions.merge()
                ).addOnCompleteListener { task ->
                    guardando = false

                    if (!task.isSuccessful) {
                        Toast.makeText(
                            context,
                            task.exception?.message
                                ?: "No se pudo actualizar el contador",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+1")
        }

        Text("Mensaje actual: $mensaje")

        OutlinedTextField(
            value = mensajeUsuario,
            onValueChange = { mensajeUsuario = it },
            enabled = !guardando,
            label = { Text("Nuevo mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = mensajeUsuario.isNotBlank() && !guardando,
            onClick = {
                guardando = true
                val nuevoMensaje = mensajeUsuario.trim()

                document.set(
                    mapOf("mensaje" to nuevoMensaje),
                    SetOptions.merge()
                ).addOnCompleteListener { task ->
                    guardando = false

                    if (task.isSuccessful) {
                        mensajeUsuario = ""
                    } else {
                        Toast.makeText(
                            context,
                            task.exception?.message
                                ?: "No se pudo actualizar el mensaje",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (guardando) "Guardando..." else "Actualizar mensaje"
            )
        }
    }
}