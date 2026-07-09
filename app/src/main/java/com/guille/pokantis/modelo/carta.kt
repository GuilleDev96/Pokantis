package com.guille.pokantis.modelo

// Define los tres tipos únicos de cartas que existirán en el juego
enum class TipoCarta {
    ESTANDAR,
    SHINY,      // Hacen pareja con todos los colores
    LEGENDARIO, // Valen 2 puntos
    OBJETO      // Cartas de entrenador/trampa
}

data class Carta(
    val id: Int,
    val nombre: String,
    val tipo: TipoCarta,
    val colorAnverso: String?, // Nullable porque los objetos pueden no tener color de puntuación
    val coloresReverso: List<String>, // Los 3 colores que se muestran en la parte trasera
    val imagenAnversoId: Int, // ID del recurso (ej. R.drawable.carta_1_anverso)
    val imagenReversoId: Int  // ID del recurso (ej. R.drawable.carta_1_reverso)
)