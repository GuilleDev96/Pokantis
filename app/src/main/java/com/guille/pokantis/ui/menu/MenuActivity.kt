package com.guille.pokantis.ui.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.guille.pokantis.R
import com.guille.pokantis.ui.jugar.JugarActivity
import com.guille.pokantis.ui.opciones.OpcionesActivity
import com.guille.pokantis.ui.perfil.PerfilActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()

        val btnJugar = findViewById<Button>(R.id.btnJugar)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnOpciones = findViewById<Button>(R.id.btnOpciones)

        btnJugar.setOnClickListener {
            startActivity(Intent(this, JugarActivity::class.java))
        }

        btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        btnOpciones.setOnClickListener {
            startActivity(Intent(this, OpcionesActivity::class.java))
        }
    }
}