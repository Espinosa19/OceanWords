package com.proyect.ocean_words.utils

import android.content.Context
import android.media.MediaPlayer
import com.proyect.ocean_words.R

class MusicManager(private val context: Context) {

    private var menuPlayer: MediaPlayer? = null
    private var levelPlayer: MediaPlayer? = null

    fun stopAllMusic() {
        menuPlayer?.pause()

        levelPlayer?.pause()
    }

    fun playMenuMusic() {
        stopLevelMusic()
        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.banda_sonora)
            menuPlayer?.isLooping = true
        }
        if (menuPlayer?.isPlaying == false) {
            menuPlayer?.start()
        }
    }

    fun stopMenuMusic() {
        menuPlayer?.pause()
    }

    fun playLevelMusic() {
        stopMenuMusic()
        if (levelPlayer == null) {
            levelPlayer = MediaPlayer.create(context, R.raw.efectos_de_sonido)
            levelPlayer?.isLooping = true
        }
        if (levelPlayer?.isPlaying == false) {
            levelPlayer?.start()
        }
    }

    fun stopLevelMusic() {
        levelPlayer?.pause()
    }

    fun destroy() {
        menuPlayer?.stop()
        menuPlayer?.release()
        menuPlayer = null

        levelPlayer?.stop()
        levelPlayer?.release()
        levelPlayer = null
    }
}