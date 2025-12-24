package com.proyect.ocean_words.viewmodels

import com.proyect.ocean_words.model.UsuariosEstado

object UserSession {
    var currentUser: UsuariosEstado? = null
    var isAuthenticated: Boolean = false

    fun clear() {
        currentUser = null
        isAuthenticated = false
    }
}
