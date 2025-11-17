package com.proyect.ocean_words.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.proyect.ocean_words.R

class MusicManager(private val context: Context) {

    private var menuPlayer: MediaPlayer? = null
    private var levelPlayer: MediaPlayer? = null

    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0

    init {
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        clickSoundId = soundPool.load(context, R.raw.click_burbuja, 1)
    }

    fun playClickSound() {
        soundPool.play(clickSoundId, 1f, 1f, 0, 0, 1f)
    }

    fun stopAllMusic() {
        menuPlayer?.pause()
        levelPlayer?.pause()
    }

    fun playMenuMusic() {
        stopLevelMusic()
        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.musica_nivel)
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
            levelPlayer = MediaPlayer.create(context, R.raw.musica_menu)
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

        soundPool.release()
    }
}