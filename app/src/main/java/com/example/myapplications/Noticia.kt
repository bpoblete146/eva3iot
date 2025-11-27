package com.example.myapplications

import com.google.firebase.firestore.DocumentId

data class Noticia(
    @DocumentId val id: String? = null,
    val titulo: String = "",
    val resumen: String = "",
    val contenido: String = "",
    val autor: String = "",
    val fecha: String = ""
)
