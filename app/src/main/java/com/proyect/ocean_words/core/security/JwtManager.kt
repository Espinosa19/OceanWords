package com.proyect.ocean_words.core.security

class JwtManager(
    private val securePrefs: SecurePreferences
) {
    fun save(token: String) = securePrefs.saveJwt(token)
    fun get(): String? = securePrefs.getJwt()
    fun clear() = securePrefs.clearJwt()
}

