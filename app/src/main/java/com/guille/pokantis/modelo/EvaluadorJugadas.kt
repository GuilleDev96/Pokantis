package com.guille.pokantis.modelo

object EvaluadorJugadas {

    /**
     * Compara las cartas de la mesa con un color específico.
     * Las cartas Shiny que ya estén en la mesa actúan siempre como comodín atrapable.
     */
    fun obtenerCoincidencias(colorJugado: String, zonaJuego: List<Carta>): List<Carta> {
        val coincidencias = mutableListOf<Carta>()

        for (cartaEnMesa in zonaJuego) {
            if (cartaEnMesa.colorAnverso == colorJugado || cartaEnMesa.tipo == TipoCarta.SHINY) {
                coincidencias.add(cartaEnMesa)
            }
        }
        return coincidencias
    }

    /**
     * Calcula los puntos: Legendarios valen 2, el resto 1.
     */
    fun calcularPuntos(cartas: List<Carta>): Int {
        var puntosTotales = 0
        for (carta in cartas) {
            puntosTotales += if (carta.tipo == TipoCarta.LEGENDARIO) 2 else 1
        }
        return puntosTotales
    }
}