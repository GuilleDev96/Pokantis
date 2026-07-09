package com.guille.pokantis.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guille.pokantis.R

class CartasPerfilAdapter(private val items: List<PerfilListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TIPO_SEPARADOR = 0
        private const val TIPO_CARTA = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PerfilListItem.Separador -> TIPO_SEPARADOR
            is PerfilListItem.CartaItem -> TIPO_CARTA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TIPO_SEPARADOR) {
            val view = inflater.inflate(R.layout.item_separador_perfil, parent, false)
            SeparadorViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_carta_perfil, parent, false)
            CartaViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is SeparadorViewHolder && item is PerfilListItem.Separador) {
            holder.tvTitulo.text = item.titulo
        } else if (holder is CartaViewHolder && item is PerfilListItem.CartaItem) {
            holder.ivCarta.setImageResource(item.carta.imagenAnversoId)
        }
    }

    override fun getItemCount(): Int = items.size

    class SeparadorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloSeparador)
    }

    class CartaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCarta: ImageView = view.findViewById(R.id.ivCarta)
    }
}