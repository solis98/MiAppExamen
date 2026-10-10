package com.example.miappexamen.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager{
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        dob: String,
        phoneNumber: String
    ): Result<Unit> {
        return try{
            val authResult = auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            val uid = authResult.user?.uid
                ?: throw Exception("No se obtuvo el ID del usuario")

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "dob" to dob,
                "phoneNumber" to phoneNumber
            )
            try {
                firestore
                    .collection("users")
                    .document(uid)
                    .set(userData)
                    .await()
            } catch (e: CancellationException){
                throw e
            } catch (e: Exception){
                throw Exception(
                    "La cuenta se creó, pero no se guardó el perfil: ${e.message}", e
                )
            }

            auth.signOut()
            Result.success(Unit)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<Unit>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    fun logoutUser() {
        auth.signOut()
    }
}