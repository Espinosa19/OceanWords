package com.proyect.ocean_words.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private val _authState = MutableStateFlow<String?>(null)
    val authState: StateFlow<String?> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isAuthenticated.value = true
                        _authState.value = "Login exitoso"
                    } else {
                        _authState.value = task.exception?.message
                        _isAuthenticated.value = false
                    }
                }
        }
    }

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isAuthenticated.value = true
                } else {
                    _authState.value = task.exception?.message
                }
            }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val userMap = mapOf(
                                "email" to email,
                                "uid" to uid
                            )
                            database.child("users").child(uid).setValue(userMap)
                        }
                        _authState.value = "Registro exitoso"
                        _isAuthenticated.value = true
                    } else {
                        _authState.value = task.exception?.message
                        _isAuthenticated.value = false
                    }
                }
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = "Se ha enviado un enlace de recuperación a su correo"
                } else {
                    _authState.value = task.exception?.localizedMessage
                }
            }
    }
}
