package com.guille.pokantis.modelo

import android.content.Context

object MazoFactory {

    fun generarMazoCompleto(context: Context): List<Carta> {
        val mazo = mutableListOf<Carta>()
        val totalCartas = 108
        val paquete = context.packageName

        for (i in 1..totalCartas) {

            val esTeamRocket = i in 106..108

            val nombreAnverso = if (esTeamRocket) "carta_rocket_anverso" else "carta_${i}_anverso"
            val nombreReverso = if (esTeamRocket) "carta_rocket_reverso" else "carta_${i}_reverso"

            val idAnverso = context.resources.getIdentifier(nombreAnverso, "drawable", paquete)
            val idReverso = context.resources.getIdentifier(nombreReverso, "drawable", paquete)

            val anversoFinal = if (idAnverso != 0) idAnverso else android.R.drawable.ic_menu_help
            val reversoFinal = if (idReverso != 0) idReverso else android.R.drawable.ic_menu_help

            val (tipo, colorAnv, coloresRev) = determinarPropiedadesCarta(i)

            mazo.add(
                Carta(
                    id = i,
                    nombre = if (esTeamRocket) "Trampa Rocket" else "Carta $i",
                    tipo = tipo,
                    colorAnverso = colorAnv,
                    coloresReverso = coloresRev,
                    imagenAnversoId = anversoFinal,
                    imagenReversoId = reversoFinal
                )
            )
        }

        return mezclarMazoPartida(mazo)
    }

    private fun mezclarMazoPartida(mazo: MutableList<Carta>): List<Carta> {
        mazo.shuffle()

        val cartasRocket = mazo.filter { it.nombre == "Trampa Rocket" }
        if (cartasRocket.isEmpty()) return mazo

        mazo.removeAll(cartasRocket)

        val mazoFinal = mazo.take(5).toMutableList()
        val mazoRestante = mazo.drop(5).toMutableList()

        mazoRestante.addAll(cartasRocket)
        mazoRestante.shuffle()

        mazoFinal.addAll(mazoRestante)

        return mazoFinal
    }

    private fun determinarPropiedadesCarta(idCarta: Int): Triple<TipoCarta, String?, List<String>> {
        return when (idCarta) {
            in 106..108 -> Triple(TipoCarta.OBJETO, null, listOf("Objeto", "Objeto", "Objeto"))

            15, 30, 45, 60, 75, 90, 105 -> Triple(TipoCarta.SHINY, "Shiny", listOf("Shiny", "Shiny", "Shiny"))

            14 -> Triple(TipoCarta.LEGENDARIO, "Agua", listOf("Agua", "Eléctrico", "Dragón"))
            29 -> Triple(TipoCarta.LEGENDARIO, "Fuego", listOf("Fuego", "Agua", "Tierra"))
            44 -> Triple(TipoCarta.LEGENDARIO, "Dragón", listOf("Psíquico", "Tierra", "Dragón"))
            59 -> Triple(TipoCarta.LEGENDARIO, "Eléctrico", listOf("Psíquico", "Eléctrico", "Dragón"))
            74 -> Triple(TipoCarta.LEGENDARIO, "Planta", listOf("Fuego", "Planta", "Tierra"))
            89 -> Triple(TipoCarta.LEGENDARIO, "Psíquico", listOf("Psíquico", "Eléctrico", "Dragón"))
            104 -> Triple(TipoCarta.LEGENDARIO, "Tierra", listOf("Eléctrico", "Tierra", "Dragón"))

            1 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Fuego", "Planta", "Agua"))
            2 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Fuego", "Agua", "Eléctrico"))
            3 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Fuego", "Agua", "Psíquico"))
            4 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Fuego", "Agua", "Tierra"))
            5 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Fuego", "Agua", "Dragón"))
            6 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Planta", "Agua", "Psiquico"))
            7 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Planta", "Agua", "Eléctrico"))
            8 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Planta", "Agua", "Tierra"))
            9 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Planta", "Agua", "Dragón"))
            10 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Agua", "Psíquico", "Eléctrico"))
            11 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Agua", "Psíquico", "Tierra"))
            12 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Agua", "Psíquico", "Dragón"))
            13 -> Triple(TipoCarta.ESTANDAR, "Agua", listOf("Agua", "Eléctrico", "Tierra"))

            16 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Planta", "Psíquico"))
            17 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Planta", "Eléctrico"))
            18 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Planta", "Tierra"))
            19 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Planta", "Dragón"))
            20 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Psíquico", "Eléctrico"))
            21 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Psíquico", "Tierra"))
            22 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Psíquico", "Dragón"))
            23 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Eléctrico", "Tierra"))
            24 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Eléctrico", "Dragón"))
            25 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Tierra", "Dragón"))
            26 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Planta", "Agua"))
            27 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Agua", "Psíquico"))
            28 -> Triple(TipoCarta.ESTANDAR, "Fuego", listOf("Fuego", "Agua", "Eléctrico"))

            31 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Fuego", "Planta", "Dragón"))
            32 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Fuego", "Agua", "Dragón"))
            33 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Fuego", "Psíquico", "Dragón"))
            34 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Fuego", "Eléctrico", "Dragón"))
            35 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Fuego", "Tierra", "Dragón"))
            36 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Planta", "Agua", "Dragón"))
            37 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Planta", "Psíquico", "Dragón"))
            38 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Planta", "Eléctrico", "Dragón"))
            39 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Planta", "Tierra", "Dragón"))
            40 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Agua", "Psíquico", "Dragón"))
            41 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Agua", "Eléctrico", "Dragón"))
            42 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Agua", "Tierra", "Dragón"))
            43 -> Triple(TipoCarta.ESTANDAR, "Dragón", listOf("Psíquico", "Eléctrico", "Dragón"))

            46 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Planta", "Psíquico", "Eléctrico"))
            47 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Planta", "Eléctrico", "Tierra"))
            48 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Psíquico", "Eléctrico", "Tierra"))
            49 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Fuego", "Planta", "Eléctrico"))
            50 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Fuego", "Agua", "Eléctrico"))
            51 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Fuego", "Psíquico", "Eléctrico"))
            52 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Fuego", "Eléctrico", "Tierra"))
            53 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Fuego", "Eléctrico", "Dragón"))
            54 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Planta", "Agua", "Eléctrico"))
            55 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Planta", "Eléctrico", "Dragón"))
            56 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Agua", "Psíquico", "Eléctrico"))
            57 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Agua", "Eléctrico", "Tierra"))
            58 -> Triple(TipoCarta.ESTANDAR, "Eléctrico", listOf("Agua", "Eléctrico", "Dragón"))

            61 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Fuego", "Planta", "Agua"))
            62 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Fuego", "Planta", "Psíquico"))
            63 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Psíquico", "Tierra"))
            64 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Tierra", "Dragón"))
            65 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Eléctrico", "Tierra"))
            66 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Eléctrico", "Dragón"))
            67 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Psíquico", "Dragón"))
            68 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Psíquico", "Eléctrico"))
            69 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Agua", "Eléctrico"))
            70 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Agua", "Tierra"))
            71 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Planta", "Agua", "Psíquico"))
            72 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Fuego", "Planta", "Dragón"))
            73 -> Triple(TipoCarta.ESTANDAR, "Planta", listOf("Fuego", "Planta", "Eléctrico"))

            76 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Fuego", "Planta", "Psíquico"))
            77 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Fuego", "Agua", "Psíquico"))
            78 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Fuego", "Psíquico", "Eléctrico"))
            79 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Fuego", "Psíquico", "Tierra"))
            80 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Fuego", "Psíquico", "Dragón"))
            81 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Planta", "Agua", "Psíquico"))
            82 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Planta", "Psíquico", "Eléctrico"))
            83 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Planta", "Psíquico", "Tierra"))
            84 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Planta", "Psíquico", "Dragón"))
            85 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Agua", "Psíquico", "Eléctrico"))
            86 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Agua", "Psíquico", "Tierra"))
            87 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Agua", "Psíquico", "Dragón"))
            88 -> Triple(TipoCarta.ESTANDAR, "Psíquico", listOf("Psíquico", "Eléctrico", "Tierra"))

            91 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Fuego", "Planta", "Tierra"))
            92 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Fuego", "Agua", "Tierra"))
            93 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Fuego", "Psíquico", "Tierra"))
            94 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Fuego", "Eléctrico", "Tierra"))
            95 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Fuego", "Tierra", "Dragón"))
            96 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Planta", "Agua", "Tierra"))
            97 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Planta", "Psíquico", "Tierra"))
            98 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Planta", "Eléctrico", "Tierra"))
            99 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Planta", "Tierra", "Dragón"))
            100 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Agua", "Psíquico", "Tierra"))
            101 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Agua", "Eléctrico", "Tierra"))
            102 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Psíquico", "Eléctrico", "Tierra"))
            103 -> Triple(TipoCarta.ESTANDAR, "Tierra", listOf("Psíquico", "Tierra", "Dragón"))

            else -> throw IllegalArgumentException("ID de carta no reconocida: $idCarta")
        }
    }
}