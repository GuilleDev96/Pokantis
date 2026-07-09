package com.guille.pokantis.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.guille.pokantis.R
import com.guille.pokantis.ui.menu.MenuActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        // Referencia a las letras desde el XML definitivo
        val ivLetras = findViewById<ImageView>(R.id.ivLetras)

        // CONFIGURACIÓN DE LA ANIMACIÓN DE ZOOM OUT (PROFESIONAL Y CON PROFUNDIDAD)

        // 1. Establecer el estado inicial (oculto, arriba y más grande)
        ivLetras.alpha = 0f              // Totalmente invisible
        ivLetras.scaleX = 1.8f           // 180% del tamaño normal (más grande)
        ivLetras.scaleY = 1.8f           // 180% del tamaño normal (más grande)
        ivLetras.translationY = -100f     // Un poco más arriba de su posición final

        // 2. Ejecutar la animación encadenada
        ivLetras.animate()
            .alpha(1f)                    // Hacer visible
            .scaleX(1.2f)                   // Reducir al tamaño normal
            .scaleY(1.2f)                   // Reducir al tamaño normal
            .translationY(0f)              // Bajar a su posición final
            .setDuration(1500)             // Un poco más larga para que se aprecie bien el efecto
            .setInterpolator(OvershootInterpolator()) // El efecto de rebote al asentarse
            .start()

        // Transición automática al Menú Principal tras 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)
    }
}