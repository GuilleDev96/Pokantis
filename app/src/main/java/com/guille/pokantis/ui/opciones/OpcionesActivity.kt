// OpcionesActivity.kt
// Añade el ImageButton y su funcionalidad en el método onCreate()

package com.guille.pokantis.ui.opciones

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.guille.pokantis.R
import com.guille.pokantis.modelo.GestorDatos
import com.guille.pokantis.modelo.ReproductorMusica

class OpcionesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones)
        supportActionBar?.hide()

        val btnVolverOpciones = findViewById<ImageButton>(R.id.btnVolverOpciones)
        btnVolverOpciones.setOnClickListener { finish() }

        val rgMusica = findViewById<RadioGroup>(R.id.rgMusica)
        val rgTapete = findViewById<RadioGroup>(R.id.rgTapete)

        when (GestorDatos.obtenerMusicaElegida(this)) {
            1 -> rgMusica.check(R.id.rbMusica1)
            2 -> rgMusica.check(R.id.rbMusica2)
            3 -> rgMusica.check(R.id.rbMusica3)
        }

        when (GestorDatos.obtenerTapeteElegido(this)) {
            1 -> rgTapete.check(R.id.rbTapete1)
            2 -> rgTapete.check(R.id.rbTapete2)
            3 -> rgTapete.check(R.id.rbTapete3)
        }

        rgMusica.setOnCheckedChangeListener { _, checkedId ->
            val opcionSeleccionada = when (checkedId) {
                R.id.rbMusica1 -> 1
                R.id.rbMusica2 -> 2
                R.id.rbMusica3 -> 3
                else -> 1
            }
            GestorDatos.guardarMusicaElegida(this, opcionSeleccionada)
            ReproductorMusica.actualizarMusica(this)
        }

        rgTapete.setOnCheckedChangeListener { _, checkedId ->
            val opcionSeleccionada = when (checkedId) {
                R.id.rbTapete1 -> 1
                R.id.rbTapete2 -> 2
                R.id.rbTapete3 -> 3
                else -> 1
            }
            GestorDatos.guardarTapeteElegido(this, opcionSeleccionada)
        }
    }
}