package com.proyect.ocean_words.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyect.ocean_words.model.EspecieEstado
import com.proyect.ocean_words.model.SlotEstado
import com.proyect.ocean_words.domain.repositories.EspecieRepository
import com.proyect.ocean_words.model.ProgresoLetra
import com.proyect.ocean_words.model.UsuariosEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.text.lowercase

class EspecieViewModel (
    level: Int,
    especie: String,
    animal: String,
    dificultad: String,
    private val usuariosViewModel: UsuariosViewModel
) : ViewModel() {
    private val repository = EspecieRepository()
    private val progresoViewModel = ProgresoViewModel()
    private val _especie = MutableStateFlow<EspecieEstado?>(null)
    val especie = _especie.asStateFlow()
    val animalSinEspacios: String = animal.trim().lowercase().replace(" ", "")
    val levelId : Int =level
    val especieId: String =especie
    private val tamanoTeclado: Int
    private val _navegarAExito = MutableLiveData<Boolean>()
    val navegarAExito: LiveData<Boolean> = _navegarAExito

    private val _pistaUsada = MutableStateFlow(false)
    val pistaUsada: StateFlow<Boolean> = _pistaUsada.asStateFlow()
    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()



    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }



    val animalRandom: String = if (dificultad != "dificil") {
        shuffleText(animalSinEspacios)
    } else {
        val letrasRandom = getTwoRandomLetters()
        shuffleText(animalSinEspacios + letrasRandom.joinToString(""))
    }

    init {
        tamanoTeclado = animalRandom.length
    }

    val visible = mutableStateListOf<Boolean>().apply {
        repeat(tamanoTeclado) { add(true) }
    }

    val respuestaJugador = mutableStateListOf<SlotEstado?>().apply {
        repeat(animalSinEspacios.length) { add(SlotEstado()) }
    }

    val botonADondeFue = mutableStateListOf<Int>().apply {
        repeat(tamanoTeclado) { add(-1) }
    }

    fun snapshotToProgreso(
        respuestaJugador: List<SlotEstado?>
    ): List<ProgresoLetra> {
        return respuestaJugador.mapIndexedNotNull { index, slot ->
            slot?.char?.let {
                ProgresoLetra(
                    char = it.toString(),
                    posicion = index,
                    correcta = slot.esCorrecto == true
                )
            }
        }
    }

    val usoLetras: StateFlow<Map<Char, Int>> =
        snapshotFlow { respuestaJugador.toList() }
            .map { slots ->
                slots
                    .mapNotNull { it?.char }
                    .groupingBy { it }
                    .eachCount()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyMap()
            )

    private fun validarPalabraCompleta() {

        val respuestaCompleta = respuestaJugador.map { it?.char ?: ' ' }.joinToString("")
        val esPalabraCorrecta = respuestaCompleta.equals(animalSinEspacios, ignoreCase = true)

        if (esPalabraCorrecta) {
            for (i in respuestaJugador.indices) {
                val currentChar = respuestaJugador[i]?.char
                respuestaJugador[i] = SlotEstado(char = currentChar, esCorrecto = true)
            }
            progresoViewModel.buscarProgresoUsuId(
                level = levelId,
                especieId = especieId,
                userId = userId,
                completado = true,
                letras = snapshotToProgreso(respuestaJugador)
            )
            _navegarAExito.value = true

        } else {
            val indicesIncorrectos = mutableListOf<Int>()
            for (i in respuestaJugador.indices) {
                val letraJugador = respuestaJugador[i]?.char
                val letraCorrecta = animalSinEspacios[i]

                val esLetraCorrectaEnPosicion = letraJugador != null &&
                        letraJugador.equals(letraCorrecta, ignoreCase = true)

                if (esLetraCorrectaEnPosicion) {
                    respuestaJugador[i] = SlotEstado(char = letraJugador, esCorrecto = true)
                } else {
                    respuestaJugador[i] = SlotEstado(char = letraJugador, esCorrecto = false)
                    indicesIncorrectos.add(i)
                }
            }

            removerLetrasIncorrectas(indicesIncorrectos)
        }
    }
    fun navegacionAExitoCompleta() {
        _navegarAExito.value = false
    }
    /**
     * Remueve las letras de las posiciones indicadas de respuestaJugador,
     * y restablece la visibilidad de los botones correspondientes.
     * * @param indicesARemover Una lista de los índices en respuestaJugador que son incorrectos.
     */
    private fun removerLetrasIncorrectas(indices: List<Int>) {
        indices.forEach { index ->
            respuestaJugador[index] = SlotEstado()
        }
    }

    fun selectLetter(char: Char, botonIndex: Int) {
        val posicion = respuestaJugador.indexOfFirst { it?.char == null }
        if (posicion == -1) return

        respuestaJugador[posicion] = SlotEstado(
            char = char,
            botonIndex = botonIndex,
            esCorrecto = null
        )

        if (respuestaJugador.none { it?.char == null }) {
            validarPalabraCompleta()
        }
    }


    fun removeLetter(responseSlotIndex: Int) {
        respuestaJugador[responseSlotIndex] = SlotEstado(char = null, esCorrecto = null)
        val originalButtonIndex = botonADondeFue.indexOf(responseSlotIndex)
        if (originalButtonIndex != -1) {
            botonADondeFue[originalButtonIndex] = -1
            visible[originalButtonIndex] = true
        }
    }

    fun goBackGame() {
        val index = respuestaJugador.indexOfLast { it?.char != null }
        if (index == -1) return

        respuestaJugador[index] = SlotEstado()
    }


    fun obtenerPista() {
        val monedas= usuariosViewModel.monedasUsuario.value?.toInt()
        val pistas = usuariosViewModel.pistasUsuario.value?.toInt()
        pistas?.let {
            if(it > 0) {

                // 1️⃣ Encuentra todos los índices donde la letra aún no está colocada
                val indicesVacios = respuestaJugador.mapIndexedNotNull { index, slot ->
                    if (slot?.char == null) index else null
                }
                if (indicesVacios.isEmpty()) return
                // 2️⃣ Elegir un índice vacío aleatorio (para que la pista sea aleatoria)
                val indexPista = indicesVacios.random()

                // 3️⃣ Colocar la letra correcta en ese índice
                val letraCorrecta = animalSinEspacios[indexPista]
                respuestaJugador[indexPista] = SlotEstado(char = letraCorrecta, esCorrecto = true)
                _pistaUsada.value = true
                viewModelScope.launch {
                    usuariosViewModel.descontarPistas(userId)

                }
            }
        }
    }


    fun resetGame() {
        visible.indices.forEach { i -> visible[i] = true }
        botonADondeFue.indices.forEach { i -> botonADondeFue[i] = -1 }
        respuestaJugador.indices.forEach { i ->
            respuestaJugador[i] = SlotEstado(char = null, esCorrecto = null)
        }
    }

    private fun getTwoRandomLetters(): List<Char> {
        val abecedario =('a'..'z')
        return List(2) { abecedario.random() } }

    private fun shuffleText(text: String): String {
        return text.toList().shuffled().joinToString("")
    }
}