package com.example.myapplications

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class VerNoticiaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var noticiaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_noticia)

        db = FirebaseFirestore.getInstance()
        noticiaId = intent.getStringExtra("noticia_id")

        val tvTitulo: TextView = findViewById(R.id.tvTituloDetalle)
        val tvResumen: TextView = findViewById(R.id.tvResumenDetalle)
        val tvContenido: TextView = findViewById(R.id.tvContenidoDetalle)
        val tvAutor: TextView = findViewById(R.id.tvAutorDetalle)
        val tvFecha: TextView = findViewById(R.id.tvFechaDetalle)
        val btnEditar: Button = findViewById(R.id.btnEditar)
        val btnEliminar: Button = findViewById(R.id.btnEliminar)

        noticiaId?.let { id ->
            db.collection("noticias").document(id).get().addOnSuccessListener { document ->
                if (document != null) {
                    val noticia = document.toObject(Noticia::class.java)
                    tvTitulo.text = noticia?.titulo
                    tvResumen.text = noticia?.resumen
                    tvContenido.text = noticia?.contenido
                    tvAutor.text = "Autor: ${noticia?.autor}"
                    tvFecha.text = "Fecha: ${noticia?.fecha}"
                } else {
                    Toast.makeText(this, "No se encontró la noticia", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar la noticia", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnEditar.setOnClickListener {
            val intent = Intent(this, AddNoticiaActivity::class.java)
            intent.putExtra("noticia_id", noticiaId)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta noticia?")
                .setPositiveButton("Eliminar") { _, _ ->
                    noticiaId?.let {
                        db.collection("noticias").document(it).delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Noticia eliminada", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al eliminar la noticia", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}