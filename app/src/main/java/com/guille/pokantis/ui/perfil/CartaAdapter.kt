package com.guille.pokantis.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guille.pokantis.R
import com.guille.pokantis.modelo.Carta

// Adaptador que conecta los datos del mazo con la interfaz del RecyclerView
class CartaAdapter(private val listaCartas: List<Carta>) : RecyclerView.Adapter<CartaAdapter.CartaViewHolder>() {

    // Vincula los elementos visuales del item_carta.xml
    class CartaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImagenCarta: ImageView = view.findViewById(R.id.ivImagenCarta)
        val tvNombreCarta: TextView = view.findViewById(R.id.tvNombreCarta)
    }

    // Crea las nuevas vistas a medida que se hace scroll
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carta, parent, false)
        return CartaViewHolder(view)
    }

    // Asigna los datos de la Carta correspondiente a la vista actual
    override fun onBindViewHolder(holder: CartaViewHolder, position: Int) {
        val carta = listaCartas[position]
        holder.ivImagenCarta.setImageResource(carta.imagenAnversoId)
        holder.tvNombreCarta.text = carta.nombre
    }

    // Devuelve la cantidad total de cartas
    override fun getItemCount(): Int = listaCartas.size
}