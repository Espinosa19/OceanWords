package com.proyect.ocean_words.core.security


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePreferences(context: Context) {

    companion object {
        private const val FILE_NAME = "secure_prefs"
        private const val KEY_JWT = "jwt_token"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // 🔐 Guardar JWT
    fun saveJwt(token: String) {
        prefs.edit().putString(KEY_JWT, token).apply()
    }

    // 📥 Obtener JWT
    fun getJwt(): String? {
        return prefs.getString(KEY_JWT, null)
    }

    // 🚪 Eliminar JWT (logout)
    fun clearJwt() {
        prefs.edit().remove(KEY_JWT).apply()
    }
}
