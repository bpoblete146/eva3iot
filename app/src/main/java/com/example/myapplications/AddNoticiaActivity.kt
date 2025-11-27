package com.example.myapplications

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoticiaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_noticia)

        db = FirebaseFirestore.getInstance()

        val etTitulo: EditText = findViewById(R.id.etTitulo)
        val etResumen: EditText = findViewById(R.id.etResumen)
        val etContenido: EditText = findViewById(R.id.etContenido)
        val etAutor: EditText = findViewById(R.id.etAutor)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val resumen = etResumen.text.toString()
            val contenido = etContenido.text.toString()
            val autor = etAutor.text.toString()
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (titulo.isNotEmpty() && resumen.isNotEmpty() && contenido.isNotEmpty() && autor.isNotEmpty()) {
                val noticia = Noticia(titulo, resumen, contenido, autor, fecha)
                db.collection("noticias")
                    .add(noticia)
                    .addOnSuccessListener { 
                        Toast.makeText(this, "Noticia guardada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar la noticia: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}