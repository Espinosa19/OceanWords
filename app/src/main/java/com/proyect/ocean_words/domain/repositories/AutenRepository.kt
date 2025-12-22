package com.proyect.ocean_words.domain.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AutenRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun loginWithEmail(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("UID del usuario es null")
    }

    /** 🔐 Login con Google */
    suspend fun loginWithGoogle(idToken: String): String {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user?.uid ?: throw Exception("UID del usuario es null")
    }
    suspend fun registerWithEmail(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("UID del usuario es null")
    }

    fun getCurrentUser() = auth.currentUser

    fun logout() = auth.signOut()
}