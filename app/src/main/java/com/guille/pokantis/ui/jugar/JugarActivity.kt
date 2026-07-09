package com.guille.pokantis.ui.jugar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.guille.pokantis.R
import com.guille.pokantis.modelo.Carta
import com.guille.pokantis.modelo.EvaluadorJugadas
import com.guille.pokantis.modelo.GestorDatos
import com.guille.pokantis.modelo.MazoFactory
import com.guille.pokantis.modelo.TipoCarta
import kotlin.random.Random

class JugarActivity : AppCompatActivity() {

    private var mazoPartida: MutableList<Carta> = mutableListOf()
    private val cartasMesaJugador = mutableListOf<Carta>()
    private val cartasMesaRival = mutableListOf<Carta>()

    private var puntosJugador = 0
    private var puntosRival = 0
    private val PUNTOS_PARA_GANAR = 10

    private var esTurnoJugador = true

    private lateinit var llZonaJuegoJugador: LinearLayout
    private lateinit var llZonaJuegoRival: LinearLayout
    private lateinit var tvPuntosJugador: TextView
    private lateinit var tvPuntosRival: TextView
    private lateinit var ivMazo: ImageView

    private lateinit var ivMoneda: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugar)
        val tapeteSeleccionado = GestorDatos.obtenerTapeteElegido(this)
        val idTapete = when (tapeteSeleccionado) {
            1 -> R.drawable.fondo_tapete // Añade estas imágenes a res/drawable
            2 -> R.drawable.fondo_tapete2
            3 -> R.drawable.fondo_tapete3
            4 -> R.drawable.fondo_tapete4
            else -> R.drawable.fondo_tapete
        }

        val contenedorPrincipal = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.contenedorPrincipalTapete)
        contenedorPrincipal.setBackgroundResource(idTapete)
        supportActionBar?.hide()

        val btnSalirPartida = findViewById<ImageButton>(R.id.btnSalirPartida)
        ivMazo = findViewById(R.id.ivMazo)
        ivMoneda = findViewById(R.id.ivMoneda)
        llZonaJuegoJugador = findViewById(R.id.llZonaJuegoJugador)
        llZonaJuegoRival = findViewById(R.id.llZonaJuegoRival)
        tvPuntosJugador = findViewById(R.id.tvPuntosJugador)
        tvPuntosRival = findViewById(R.id.tvPuntosRival)

        mazoPartida = MazoFactory.generarMazoCompleto(this).toMutableList()
        actualizarMazoUI()

        btnSalirPartida.setOnClickListener { finish() }

        ivMazo.setOnClickListener {
            if (!esTurnoJugador) {
                Toast.makeText(this, "Es el turno del rival", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mazoPartida.isNotEmpty()) {
                ivMazo.animate()
                    .scaleX(0.9f).scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        ivMazo.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                        if (mazoPartida[0].tipo == TipoCarta.OBJETO) {
                            procesarRoboDeCarta(intencionPuntuar = true, esJugador = true)
                        } else {
                            preguntarIntencionJugada()
                        }
                    }.start()
            } else {
                Toast.makeText(this, "El mazo está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animarMoneda(resultadoEsCara: Boolean, alTerminar: () -> Unit) {
        ivMoneda.animate()
            .rotationYBy(720f)
            .setDuration(1000)
            .withEndAction {
                val icono = if (resultadoEsCara) R.drawable.icono_moneda else R.drawable.icono_moneda_cruz
                ivMoneda.setImageResource(icono)
                alTerminar()
            }
            .start()
    }

    private fun actualizarMazoUI() {
        if (mazoPartida.isNotEmpty()) {
            ivMazo.setImageResource(mazoPartida[0].imagenReversoId)
        } else {
            ivMazo.setImageResource(android.R.color.transparent)
        }
    }

    private fun preguntarIntencionJugada() {
        val opciones = listOf("Jugar para mí / Puntuar", "Jugar para el rival / Robar")
        mostrarMenuAccionPro("¿A qué zona jugarás la carta?", opciones) { index ->
            procesarRoboDeCarta(index == 0, esJugador = true)
        }
    }

    private fun procesarRoboDeCarta(intencionPuntuar: Boolean, esJugador: Boolean) {
        val cartaRobada = mazoPartida.removeAt(0)

        actualizarMazoUI()

        mostrarCartaRobadaTemporal(cartaRobada) {
            ejecutarLogicaCarta(cartaRobada, intencionPuntuar, esJugador)
        }
    }

    private fun mostrarCartaRobadaTemporal(carta: Carta, onTerminado: () -> Unit) {
        val vistaCarta = LayoutInflater.from(this).inflate(R.layout.item_carta_robada, null)
        val ivCarta = vistaCarta.findViewById<ImageView>(R.id.ivCartaRobada)

        ivCarta.setImageResource(carta.imagenAnversoId)

        val dialog = AlertDialog.Builder(this)
            .setView(vistaCarta)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) dialog.dismiss()
            onTerminado()
        }, 1500)
    }

    private fun ejecutarLogicaCarta(cartaRobada: Carta, intencionPuntuar: Boolean, esJugador: Boolean) {
        if (cartaRobada.tipo == TipoCarta.OBJETO) {
            ejecutarMecanicaTeamRocket(cartaRobada, esJugador)
            return
        }

        val zonaObjetivo = if (intencionPuntuar == esJugador) cartasMesaJugador else cartasMesaRival

        if (cartaRobada.tipo == TipoCarta.SHINY) {
            if (esJugador) {
                mostrarSelectorColorShiny(cartaRobada, zonaObjetivo, intencionPuntuar)
            } else {
                iaEligeColorShiny(cartaRobada, zonaObjetivo, intencionPuntuar)
            }
        } else {
            val colorCarta = cartaRobada.colorAnverso ?: ""
            val coincidencias = EvaluadorJugadas.obtenerCoincidencias(colorCarta, zonaObjetivo)
            procesarResolucionJugada(cartaRobada, coincidencias, zonaObjetivo, intencionPuntuar, esJugador)
        }
    }

    private fun mostrarSelectorColorShiny(cartaShiny: Carta, zonaObjetivo: MutableList<Carta>, intencionPuntuar: Boolean) {
        val coloresDisponibles = zonaObjetivo.mapNotNull { it.colorAnverso }.filter { it != "Shiny" }.distinct()

        if (coloresDisponibles.isEmpty()) {
            procesarResolucionJugada(cartaShiny, emptyList(), zonaObjetivo, intencionPuntuar, esJugador = true)
            return
        }

        mostrarMenuAccionPro("¡Shiny! Elige tipo a emparejar:", coloresDisponibles) { index ->
            val colorElegido = coloresDisponibles[index]
            val coincidencias = EvaluadorJugadas.obtenerCoincidencias(colorElegido, zonaObjetivo)
            procesarResolucionJugada(cartaShiny, coincidencias, zonaObjetivo, intencionPuntuar, esJugador = true)
        }
    }

    private fun iaEligeColorShiny(cartaShiny: Carta, zonaObjetivo: MutableList<Carta>, intencionPuntuar: Boolean) {
        val coloresDisponibles = zonaObjetivo.mapNotNull { it.colorAnverso }.filter { it != "Shiny" }.distinct()

        if (coloresDisponibles.isEmpty()) {
            procesarResolucionJugada(cartaShiny, emptyList(), zonaObjetivo, intencionPuntuar, esJugador = false)
            return
        }

        var mejorColor = coloresDisponibles[0]
        var maxPuntos = -1

        for (color in coloresDisponibles) {
            val coincidenciasTemp = EvaluadorJugadas.obtenerCoincidencias(color, zonaObjetivo)
            val puntos = EvaluadorJugadas.calcularPuntos(coincidenciasTemp)
            if (puntos > maxPuntos) {
                maxPuntos = puntos
                mejorColor = color
            }
        }

        val coincidenciasFinales = EvaluadorJugadas.obtenerCoincidencias(mejorColor, zonaObjetivo)
        Toast.makeText(this, "Rival usa Shiny como: $mejorColor", Toast.LENGTH_SHORT).show()
        procesarResolucionJugada(cartaShiny, coincidenciasFinales, zonaObjetivo, intencionPuntuar, esJugador = false)
    }

    private fun procesarResolucionJugada(cartaRobada: Carta, coincidencias: List<Carta>, zonaObjetivo: MutableList<Carta>, intencionPuntuar: Boolean, esJugador: Boolean) {
        val colorJugado = cartaRobada.colorAnverso ?: "Comodín"

        if (coincidencias.isNotEmpty()) {
            zonaObjetivo.removeAll(coincidencias)
            val puntosGanados = EvaluadorJugadas.calcularPuntos(coincidencias + listOf(cartaRobada))

            if (esJugador) {
                if (intencionPuntuar) {
                    puntosJugador += puntosGanados
                    Toast.makeText(this, "Pareja ($colorJugado). Ganas $puntosGanados pts", Toast.LENGTH_SHORT).show()
                } else {
                    cartasMesaJugador.addAll(coincidencias)
                    cartasMesaJugador.add(cartaRobada)
                    Toast.makeText(this, "¡Robas con $colorJugado!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (intencionPuntuar) {
                    puntosRival += puntosGanados
                    Toast.makeText(this, "El rival puntúa con $colorJugado", Toast.LENGTH_SHORT).show()
                } else {
                    cartasMesaRival.addAll(coincidencias)
                    cartasMesaRival.add(cartaRobada)
                    Toast.makeText(this, "El rival te roba con $colorJugado", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            zonaObjetivo.add(cartaRobada)

            if (esJugador) {
                val msj = if (intencionPuntuar) "No tienes el tipo $colorJugado, la colocas tu zona de juego" else "El rival no tiene el tipo $colorJugado, le colocas la carta en su zona de juego"
                Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
            } else {
                val msj = if (intencionPuntuar) "El rival no tiene el tipo $colorJugado, la coloca en su zona de juego" else "El rival falla al intentar robarte con el tipo $colorJugado, te coloca la carta en tu zona de juego"
                Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
            }
        }

        finalizarTurno(esJugador)
    }

    private fun finalizarTurno(turnoActualEsJugador: Boolean) {
        ivMoneda.setImageResource(R.drawable.icono_moneda)

        actualizarMesaUI()
        actualizarMazoUI()

        if (comprobarVictoria()) return

        if (turnoActualEsJugador) {
            esTurnoJugador = false
            Handler(Looper.getMainLooper()).postDelayed({
                jugarTurnoRival()
            }, 1500)
        } else {
            esTurnoJugador = true
        }
    }

    private fun jugarTurnoRival() {
        if (mazoPartida.isEmpty()) return

        val cartaTop = mazoPartida[0]

        if (cartaTop.tipo == TipoCarta.OBJETO) {
            procesarRoboDeCarta(intencionPuntuar = true, esJugador = false)
            return
        }

        var valorEsperadoPuntuar = 0
        var valorEsperadoRobar = 0

        for (colorVisible in cartaTop.coloresReverso) {
            if (colorVisible == "Shiny") {
                valorEsperadoPuntuar += EvaluadorJugadas.calcularPuntos(cartasMesaRival) + 1
                valorEsperadoRobar += EvaluadorJugadas.calcularPuntos(cartasMesaJugador) + 1
                continue
            }

            val coincidenRival = cartasMesaRival.filter { it.colorAnverso == colorVisible || it.tipo == TipoCarta.SHINY }
            if (coincidenRival.isNotEmpty()) {
                valorEsperadoPuntuar += EvaluadorJugadas.calcularPuntos(coincidenRival) + 1
            }

            val coincidenJugador = cartasMesaJugador.filter { it.colorAnverso == colorVisible || it.tipo == TipoCarta.SHINY }
            if (coincidenJugador.isNotEmpty()) {
                valorEsperadoRobar += EvaluadorJugadas.calcularPuntos(coincidenJugador) + 1
            }
        }

        val intencionPuntuar = when {
            valorEsperadoPuntuar > valorEsperadoRobar -> true
            valorEsperadoRobar > valorEsperadoPuntuar -> false
            else -> kotlin.random.Random.nextBoolean()
        }

        procesarRoboDeCarta(intencionPuntuar, esJugador = false)
    }

    private fun comprobarVictoria(): Boolean {
        if (puntosJugador >= PUNTOS_PARA_GANAR) {
            GestorDatos.registrarVictoria(this)
            mostrarDialogoVictoria("¡Has ganado la partida!")
            return true
        } else if (puntosRival >= PUNTOS_PARA_GANAR) {
            GestorDatos.registrarDerrota(this)
            mostrarDialogoVictoria("El rival ha ganado la partida.")
            return true
        }
        return false
    }

    private fun mostrarDialogoVictoria(mensaje: String) {
        mostrarMenuAccionPro(mensaje, listOf("Salir")) {
            finish()
        }
    }

    private fun ejecutarMecanicaTeamRocket(carta: Carta, esJugador: Boolean) {
        if (esJugador) {
            mostrarDialogoCaraCruz()
        } else {
            val eleccionIA = kotlin.random.Random.nextBoolean()
            val resultadoMoneda = kotlin.random.Random.nextBoolean()

            animarMoneda(resultadoMoneda) {
                if (eleccionIA == resultadoMoneda) {
                    Toast.makeText(this, "Rival acertó la moneda. ¡Usa el Team Rocket!", Toast.LENGTH_SHORT).show()
                    robarCartasIARocket()
                } else {
                    Toast.makeText(this, "Rival falló la moneda. Pierde su turno.", Toast.LENGTH_SHORT).show()
                    finalizarTurno(false)
                }
            }
        }
    }

    private fun mostrarDialogoCaraCruz() {
        val opciones = listOf("Cara", "Cruz")
        mostrarMenuAccionPro("Team Rocket: ¿Cara o Cruz?", opciones) { index ->
            val eleccionJugador = (index == 0)
            lanzarMonedaJugador(eleccionJugador)
        }
    }

    private fun lanzarMonedaJugador(eleccionJugador: Boolean) {
        val resultadoMoneda = kotlin.random.Random.nextBoolean()

        animarMoneda(resultadoMoneda) {
            val nombreResultado = if (resultadoMoneda) "Cara" else "Cruz"

            if (eleccionJugador == resultadoMoneda) {
                Toast.makeText(this, "¡Salió $nombreResultado! Has acertado.", Toast.LENGTH_SHORT).show()
                mostrarSelectorRoboRocket()
            } else {
                Toast.makeText(this, "¡Salió $nombreResultado! Has fallado y pierdes el turno.", Toast.LENGTH_SHORT).show()
                finalizarTurno(true)
            }
        }
    }

    private fun mostrarSelectorRoboRocket() {
        val coloresRival = cartasMesaRival.mapNotNull { it.colorAnverso }.filter { it != "Shiny" }.distinct()

        if (coloresRival.isEmpty()) {
            Toast.makeText(this, "El rival no tiene cartas en juego.", Toast.LENGTH_SHORT).show()
            finalizarTurno(true)
            return
        }

        mostrarMenuAccionPro("¡Éxito! Elige el tipo de carta a robar:", coloresRival) { index ->
            val colorElegido = coloresRival[index]
            ejecutarRoboRocket(colorElegido, esJugador = true)
        }
    }

    private fun robarCartasIARocket() {
        val coloresJugador = cartasMesaJugador.mapNotNull { it.colorAnverso }.filter { it != "Shiny" }.distinct()

        if (coloresJugador.isEmpty()) {
            finalizarTurno(false)
            return
        }

        var mejorColor = coloresJugador[0]
        var maxCartas = -1

        for (color in coloresJugador) {
            val cantidadCartas = cartasMesaJugador.count { it.colorAnverso == color }
            if (cantidadCartas > maxCartas) {
                maxCartas = cantidadCartas
                mejorColor = color
            }
        }

        ejecutarRoboRocket(mejorColor, esJugador = false)
    }

    private fun ejecutarRoboRocket(colorElegido: String, esJugador: Boolean) {
        if (esJugador) {
            val cartasRobadas = cartasMesaRival.filter { it.colorAnverso == colorElegido }
            cartasMesaRival.removeAll(cartasRobadas)
            cartasMesaJugador.addAll(cartasRobadas)
            Toast.makeText(this, "Has robado las cartas del tipo $colorElegido", Toast.LENGTH_SHORT).show()
            finalizarTurno(true)
        } else {
            val cartasRobadas = cartasMesaJugador.filter { it.colorAnverso == colorElegido }
            cartasMesaJugador.removeAll(cartasRobadas)
            cartasMesaRival.addAll(cartasRobadas)
            Toast.makeText(this, "El rival te robó las cartas del tipo $colorElegido", Toast.LENGTH_SHORT).show()
            finalizarTurno(false)
        }
    }

    private fun actualizarMesaUI() {
        tvPuntosJugador.text = "Tú: $puntosJugador"
        tvPuntosRival.text = "Rival: $puntosRival"

        llZonaJuegoJugador.removeAllViews()
        llZonaJuegoRival.removeAllViews()

        agruparYMostrarCartas(cartasMesaJugador, llZonaJuegoJugador)
        agruparYMostrarCartas(cartasMesaRival, llZonaJuegoRival)
    }

    // Reemplaza esta función en JugarActivity.kt
    private fun agruparYMostrarCartas(cartas: List<Carta>, zona: LinearLayout) {
        zona.weightSum = 6f
        val grupos = cartas.groupBy { it.colorAnverso ?: "Comodín" }.values.toList()

        for (i in 0 until 6) {
            val vistaZona = LayoutInflater.from(this).inflate(R.layout.item_zona_carta, zona, false)
            val cvPlaceholder = vistaZona.findViewById<androidx.cardview.widget.CardView>(R.id.cvPlaceholder)
            val cvCartaReal = vistaZona.findViewById<androidx.cardview.widget.CardView>(R.id.cvCartaReal)
            val ivCarta = vistaZona.findViewById<ImageView>(R.id.ivCartaZona)
            val tvContador = vistaZona.findViewById<TextView>(R.id.tvContador)

            cvPlaceholder.visibility = android.view.View.VISIBLE

            if (i < grupos.size) {
                val grupoCartas = grupos[i]
                val cantidad = grupoCartas.size

                cvCartaReal.visibility = android.view.View.VISIBLE
                ivCarta.setImageResource(grupoCartas.last().imagenAnversoId)

                // Efecto de aparición/robo
                cvCartaReal.alpha = 0f
                cvCartaReal.scaleX = 0.5f
                cvCartaReal.scaleY = 0.5f
                cvCartaReal.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .setStartDelay((i * 50).toLong())
                    .start()

                if (cantidad > 1) {
                    tvContador.visibility = android.view.View.VISIBLE
                    tvContador.text = cantidad.toString()
                } else {
                    tvContador.visibility = android.view.View.GONE
                }
            } else {
                cvCartaReal.visibility = android.view.View.GONE
                tvContador.visibility = android.view.View.GONE
            }

            zona.addView(vistaZona)
        }
    }
    private fun mostrarMenuAccionPro(titulo: String, opciones: List<String>, onSeleccion: (Int) -> Unit) {
        val vista = LayoutInflater.from(this).inflate(R.layout.dialog_opciones_juego, null)
        val tvTitulo = vista.findViewById<TextView>(R.id.tvDialogoTitulo)
        val llContenedor = vista.findViewById<LinearLayout>(R.id.llContenedorBotones)

        tvTitulo.text = titulo

        val dialog = AlertDialog.Builder(this)
            .setView(vista)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        opciones.forEachIndexed { index, textoOpcion ->
            val vistaBoton = LayoutInflater.from(this).inflate(R.layout.item_boton_dialogo, llContenedor, false) as Button
            vistaBoton.text = textoOpcion.uppercase()
            vistaBoton.setOnClickListener {
                dialog.dismiss()
                onSeleccion(index)
            }
            llContenedor.addView(vistaBoton)
        }

        dialog.show()
    }
}