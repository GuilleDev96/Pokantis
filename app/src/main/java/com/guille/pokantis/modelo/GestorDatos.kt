package com.guille.pokantis.modelo

import android.content.Context
import android.content.SharedPreferences

object GestorDatos {

    private const val PREFS_NAME = "pokantis_datos_offline"
    private const val KEY_VICTORIAS = "victorias_jugador"

    // Obtiene la instancia de SharedPreferences para almacenamiento local sin internet
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // 1. En GestorDatos.kt (Añade estas funciones)
    fun registrarVictoria(context: Context) {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        val victoriasActuales = prefs.getInt("victorias", 0)
        prefs.edit().putInt("victorias", victoriasActuales + 1).apply()
    }

    fun registrarDerrota(context: Context) {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        val derrotasActuales = prefs.getInt("derrotas", 0)
        prefs.edit().putInt("derrotas", derrotasActuales + 1).apply()
    }

    fun obtenerVictorias(context: Context): Int {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("victorias", 0)
    }

    fun obtenerDerrotas(context: Context): Int {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("derrotas", 0)
    }

    fun obtenerMusicaElegida(context: Context): Int {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("musica_elegida", 1)
    }

    fun guardarMusicaElegida(context: Context, opcion: Int) {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("musica_elegida", opcion).apply()
    }

    fun obtenerTapeteElegido(context: Context): Int {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("tapete_elegido", 1)
    }

    fun guardarTapeteElegido(context: Context, opcion: Int) {
        val prefs = context.getSharedPreferences("PokantisPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("tapete_elegido", opcion).apply()
    }
}