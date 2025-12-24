package com.proyect.ocean_words.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyect.ocean_words.view.auth.isValidEmailR
import com.proyect.ocean_words.view.auth.isValidName
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.domain.repositories.AutenRepository
import com.proyect.ocean_words.domain.repositories.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.proyect.ocean_words.core.security.JwtManager
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val jwtManager: JwtManager

) : ViewModel() {

    val usuarioRepository = UsuarioRepository()
    val autRepository = AutenRepository()
    private val _usuarioLiveData = MutableLiveData<UsuariosEstado?>()
    val usuarioLiveData: LiveData<UsuariosEstado?> = _usuarioLiveData

    private val _authState = MutableStateFlow<String?>(null)
    val authState: StateFlow<String?> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val uid = autRepository.loginWithEmail(email, password)


                usuarioRepository.buscarUsuarioPorId(uid).collect { usuario ->
                    if (usuario != null) {


                        val user  = autRepository.getCurrentUser()
                        val tokenResult = user?.getIdToken(true)?.await()
                        val token = tokenResult?.token

                        token?.let { jwtManager.save(it) }
                        _usuarioLiveData.value = usuario
                        UserSession.currentUser = usuario
                        UserSession.isAuthenticated = true

                        _isAuthenticated.value = true
                        _authState.value = "Login exitoso"
                    } else {
                        _isAuthenticated.value = false
                        UserSession.isAuthenticated = false
                        _authState.value = "Usuario no encontrado"
                    }
                }

            } catch (e: Exception) {
                _authState.value = e.message
                _isAuthenticated.value = false
                UserSession.isAuthenticated = false
            }
        }
    }



    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                if (idToken.isBlank()) {
                    _authState.value = "Token de Google inválido"
                    _isAuthenticated.value = false
                    return@launch
                }

                // 🔹 1. Login Firebase
                val uid = autRepository.loginWithGoogle(idToken)

                // 🔹 2. Buscar usuario en Firestore
                val usuario = usuarioRepository
                    .buscarUsuarioPorId(uid)
                    .first()

                // 🔹 3. Si no existe → crear usuario
                val finalUser = if (usuario == null) {

                    val firebaseUser = autRepository.getCurrentUser()

                    val nombre = firebaseUser?.displayName ?: "Usuario"
                    val correo = firebaseUser?.email ?: ""
                    val usuario=usuarioRepository.crearUsuario(uid, correo,nombre)
                    usuario

                } else {
                    usuario
                }

                val tokenResult = autRepository
                    .getCurrentUser()
                    ?.getIdToken(true)
                    ?.await()

                tokenResult?.token?.let { jwtManager.save(it) }

                // 🔹 4. Guardar sesión
                _usuarioLiveData.value = usuario
                UserSession.currentUser = usuario
                UserSession.isAuthenticated = true

                _isAuthenticated.value = true
                _authState.value = "Login con Google exitoso"

            } catch (e: Exception) {
                UserSession.isAuthenticated = false
                _isAuthenticated.value = false
                _authState.value = e.localizedMessage ?: "Error al iniciar sesión con Google"
            }
        }
    }
    fun restoreSessionIfPossible() {
        viewModelScope.launch {
            try {
                val user = autRepository.getCurrentUser()

                if (user == null) {
                    _isAuthenticated.value = false
                    return@launch
                }

                val usuario = usuarioRepository
                    .buscarUsuarioPorId(user.uid)
                    .first()

                if (usuario == null) {
                    _isAuthenticated.value = false
                    UserSession.isAuthenticated = false
                    return@launch
                }

                _usuarioLiveData.value = usuario
                UserSession.currentUser = usuario
                UserSession.isAuthenticated = true
                _isAuthenticated.value = true

            } catch (e: Exception) {
                _isAuthenticated.value = false
                UserSession.isAuthenticated = false
            }
        }
    }
    fun checkSession() {
        viewModelScope.launch {
            val user = autRepository.getCurrentUser()
            if (user != null) {
                restoreSessionIfPossible()
            } else {
                _isAuthenticated.value = false
            }
        }
    }


    fun validarRegistro(
        fullname: String,
        email: String,
        password: String
    ): String? {

        return when {
            fullname.isBlank() ->
                "Ingrese su nombre completo"

            !isValidName(fullname) ->
                "El nombre solo puede contener letras"

            email.isBlank() ->
                "Ingrese correo electrónico"

            !isValidEmailR(email) ->
                "Correo inválido"

            password.isBlank() ->
                "Ingrese contraseña"

            password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"

            else -> null // ✅ Todo correcto
        }
    }


    fun registerUser(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                // 1. Crear usuario en Firebase Auth
                val uid = autRepository.registerWithEmail(email, password)


                usuarioRepository.crearYObservarUsuario(uid, email,nombre)

                // 4. Actualizar estado UI
                _isAuthenticated.value = true
                _authState.value = "Registro exitoso"

            } catch (e: Exception) {
                UserSession.isAuthenticated = false
                _isAuthenticated.value = false
                _authState.value = e.localizedMessage
            }
        }
    }
//
//    fun resetPassword(email: String) {
//        auth.sendPasswordResetEmail(email)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    _authState.value = "Se ha enviado un enlace de recuperación a su correo"
//                } else {
//                    _authState.value = task.exception?.localizedMessage
//                }
//            }
//    }

    fun logout() {
        autRepository.logout()
        UserSession.clear()
        _usuarioLiveData.value = null
        _isAuthenticated.value = false
        _authState.value = "Sesión cerrada"
    }
}
