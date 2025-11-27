package com.example.myapplications

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VerNoticiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_noticia)

        val tvTituloDetalle: TextView = findViewById(R.id.tvTituloDetalle)
        val tvResumenDetalle: TextView = findViewById(R.id.tvResumenDetalle)
        val tvContenidoDetalle: TextView = findViewById(R.id.tvContenidoDetalle)
        val tvAutorDetalle: TextView = findViewById(R.id.tvAutorDetalle)
        val tvFechaDetalle: TextView = findViewById(R.id.tvFechaDetalle)

        val titulo = intent.getStringExtra("titulo")
        val resumen = intent.getStringExtra("resumen")
        val contenido = intent.getStringExtra("contenido")
        val autor = intent.getStringExtra("autor")
        val fecha = intent.getStringExtra("fecha")

        tvTituloDetalle.text = titulo
        tvResumenDetalle.text = resumen
        tvContenidoDetalle.text = contenido
        tvAutorDetalle.text = "Autor: $autor"
        tvFechaDetalle.text = "Fecha: $fecha"
    }
}