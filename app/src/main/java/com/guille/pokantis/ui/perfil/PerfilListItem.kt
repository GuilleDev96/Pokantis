package com.guille.pokantis.ui.perfil

import com.guille.pokantis.modelo.Carta

sealed class PerfilListItem {
    data class Separador(val titulo: String) : PerfilListItem()
    data class CartaItem(val carta: Carta) : PerfilListItem()
}