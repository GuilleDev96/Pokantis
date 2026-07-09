package com.guille.pokantis.modelo

import android.content.Context
import android.media.MediaPlayer
import com.guille.pokantis.R

object ReproductorMusica {
    private var mediaPlayer: MediaPlayer? = null

    fun iniciarMusica(context: Context) {
        if (mediaPlayer?.isPlaying == true) return

        val opcion = GestorDatos.obtenerMusicaElegida(context)
        val cancionId = when (opcion) {
            1 -> R.raw.pokemon_amarillo
            2 -> R.raw.pokemon_gold_silver
            3 -> R.raw.pokemon_rojo_fuego
            else -> R.raw.pokemon_amarillo
        }

        mediaPlayer = MediaPlayer.create(context.applicationContext, cancionId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun actualizarMusica(context: Context) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        iniciarMusica(context)
    }
}