package com.Auth

import android.util.Log
import android.util.Log.e
import com.google.android.gms.common.util.CollectionUtils.mapOf
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AuthState
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapOf

class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    name = firebaseUser.displayName ?: "Usuario"
                )
                Result.success(user)
            } ?: Result.failure(Exception("Error al iniciar sesión"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                saveUserData(firebaseUser.uid, email, name)

                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    name = name
                )
                Result.success(user)
            } ?: Result.failure(Exception("Error al registrar el usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserData(uid: String, email: String, name: String) {
        try {
            Firebase.firestore.collection("users").document(uid).set(mapOf(
                "name" to name,
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )).await()
        } catch (e: Exception) {
           Log.e("AuthRepository", "Error al guardar los datos del usuario", e)
        }
    }

    fun getCurrentUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: "Usuario"
            )
        }
    }

    suspend fun logout(): Result<Boolean> {
        return try {
            auth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAuthState(): Flow<AuthState> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                val currentUser = User(
                    uid = user.uid,
                    email = user.email ?: "",
                    name = user.displayName ?: "Usuario"
                )
                trySend(AuthState.Authenticated(currentUser))
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }

        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}