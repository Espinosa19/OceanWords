package com.proyect.ocean_words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.proyect.ocean_words.domain.repositories.UsuarioRepository
import com.proyect.ocean_words.model.UsuariosEstado
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuariosViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()
    private val RECHARGE_COOLDOWN_MS = 1 * 60 * 1000L
    private val MAX_LIVES = 3

    private val _vidas = MutableStateFlow<List<Boolean>>(emptyList())
    val vidas: StateFlow<List<Boolean>> = _vidas.asStateFlow()

    private val _monedasUsuario = MutableStateFlow<Int?>(null)
    val monedasUsuario: StateFlow<Int?> = _monedasUsuario.asStateFlow()

    private val _lastLifeLossTime = MutableStateFlow<Long?>(null)

    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()
    fun observarMonedasVidasUsuario(id: String) {
        viewModelScope.launch {
            usuarioRepository.observarUsuario(id).collect { usuario ->
                _monedasUsuario.value = usuario?.monedas_obtenidas
                _vidas.value = usuario?.vidas_restantes ?: listOf(true, true, true)
            }
        }
    }

    val timeToNextLife: StateFlow<String> =
        _lastLifeLossTime
            .filterNotNull()
            .flatMapLatest { lastLoss ->
                flow {
                    while (true) {
                        val elapsed = System.currentTimeMillis() - lastLoss
                        val timeLeft = RECHARGE_COOLDOWN_MS - elapsed

                        if (timeLeft > 0) {
                            emit(formatTime(timeLeft))
                            delay(1000)
                        } else {
                            emit("00:00")

                            // 🔥 Regenerar EXACTAMENTE UNA VEZ
                            regenerateOneLifeAndCheckRestart(
                                FirebaseAuth.getInstance().currentUser?.uid ?: return@flow
                            )
                            break
                        }
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                ""
            )

    fun checkAndRegenerateLife(userId: String) {

        val lastLoss = _lastLifeLossTime.value ?: return
        val elapsed = System.currentTimeMillis() - lastLoss

        if (elapsed >= RECHARGE_COOLDOWN_MS) {
            regenerateOneLifeAndCheckRestart(userId)
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun perderVidaGlobal(id: String) {
        val index = _vidas.value.indexOfLast { it }
        if (index == -1) return

        val nuevasVidas = _vidas.value.toMutableList().apply {
            this[index] = false
        }

        _vidas.value = nuevasVidas

        viewModelScope.launch {
            usuarioRepository.actualizarVidas(id, nuevasVidas)
        }

        if (_lastLifeLossTime.value == null) {
            _lastLifeLossTime.value = System.currentTimeMillis()
        }
    }

    private fun regenerateOneLifeAndCheckRestart(userId: String) {

        _vidas.update { currentVidas ->
            val firstLostIndex = currentVidas.indexOf(false)

            if (firstLostIndex == -1) {
                _lastLifeLossTime.value = null
                return@update currentVidas
            }

            val newVidas = currentVidas.toMutableList().apply {
                this[firstLostIndex] = true
            }

            // Manejo del tiempo
            if (newVidas.count { it } < MAX_LIVES) {
                _lastLifeLossTime.value = System.currentTimeMillis()
            } else {
                _lastLifeLossTime.value = null
            }

            // 🔥 SIEMPRE guardar en Firebase
            viewModelScope.launch {
                usuarioRepository.actualizarVidas(userId, newVidas)
            }

            newVidas
        }
    }

    fun reiniciarVidas() {
        _vidas.value = listOf(true, true, true)
        _lastLifeLossTime.value = null
    }
}
