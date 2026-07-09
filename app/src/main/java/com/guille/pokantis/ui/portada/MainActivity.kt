package com.guille.pokantis.ui.portada

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.guille.pokantis.ui.menu.MenuActivity
import com.guille.pokantis.R
import com.guille.pokantis.modelo.ReproductorMusica

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ocultar la barra superior (ActionBar) para que sea pantalla completa
        supportActionBar?.hide()

        // Handler para retrasar la ejecución de código (3000 milisegundos = 3 segundos)
        Handler(Looper.getMainLooper()).postDelayed({

            // Intent define la transición de esta actividad (this) a la actividad del Menú
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            // finish() destruye esta pantalla para que el usuario no pueda volver a ella
            // pulsando el botón "Atrás" del móvil.
            finish()

        }, 3000)
    }

}