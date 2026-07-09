// PerfilActivity.kt
package com.guille.pokantis.ui.perfil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guille.pokantis.R
import com.guille.pokantis.modelo.Carta
import com.guille.pokantis.modelo.GestorDatos
import com.guille.pokantis.modelo.MazoFactory // Asumiendo que obtienes las cartas de aquí de momento

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()

        val btnAtras = findViewById<ImageButton>(R.id.btnVolver)
        btnAtras.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        configurarRecyclerView()
    }
    override fun onResume() {
        super.onResume()

        val tvVictorias = findViewById<TextView>(R.id.tvVictorias) // Cambia el ID si es diferente en tu XML
        val tvDerrotas = findViewById<TextView>(R.id.tvDerrotas)   // Cambia el ID si es diferente en tu XML

        val victorias = GestorDatos.obtenerVictorias(this)
        val derrotas = GestorDatos.obtenerDerrotas(this)

        tvVictorias.text = victorias.toString()
        tvDerrotas.text = derrotas.toString()
    }

    private fun configurarRecyclerView() {
        val rvCartas = findViewById<RecyclerView>(R.id.rvMazoCartas)

        // 1. Obtener datos
        val listaCartas = MazoFactory.generarMazoCompleto(this)

        // 2. Ordenar por Tipo, luego por Color (manejando nulos) y finalmente por ID
        val cartasOrdenadas = listaCartas.sortedWith(
            compareBy(
            { it.tipo },
            { it.colorAnverso ?: "" },
            { it.id }
        ))

        // 3. Procesar la lista inyectando separadores precisos
        val itemsProcesados = mutableListOf<PerfilListItem>()
        var categoriaActual = ""

        for (carta in cartasOrdenadas) {

            // Definir el texto del separador basándonos en el tipo y el color
            val tituloGrupo = when (carta.tipo.name) {
                "ESTANDAR" -> "ESTÁNDAR - ${carta.colorAnverso?.uppercase()}"
                "LEGENDARIO" -> "LEGENDARIOS - ${carta.colorAnverso?.uppercase()}"
                "SHINY" -> "CARTAS SHINY"
                "OBJETO" -> "OBJETOS"
                else -> carta.tipo.name
            }

            // Si cambiamos de grupo (Tipo o Color), añadimos un nuevo separador
            if (tituloGrupo != categoriaActual) {
                itemsProcesados.add(PerfilListItem.Separador(tituloGrupo))
                categoriaActual = tituloGrupo
            }
            itemsProcesados.add(PerfilListItem.CartaItem(carta))
        }

        // 4. Configurar el adaptador
        val adapter = CartasPerfilAdapter(itemsProcesados)
        rvCartas.adapter = adapter

        // 5. Configurar el GridLayoutManager (3 columnas)
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    0 -> 3 // El separador ocupa la fila completa (3 columnas)
                    else -> 1 // La carta ocupa 1 columna
                }
            }
        }
        rvCartas.layoutManager = layoutManager
    }
}